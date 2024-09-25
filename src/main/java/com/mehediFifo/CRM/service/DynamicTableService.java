package com.mehediFifo.CRM.service;

import com.mehediFifo.CRM.DTO.CampaignDataDTO;
import com.mehediFifo.CRM.repository.DataTablesRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.ResultSet;
import java.util.concurrent.*;
import java.util.stream.Stream;


@Service
public class DynamicTableService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataTablesRepository repository;

    @Transactional
    public void createTable(String tableName, List<String> columns) {
        List<String> defaultColumns = List.of(
                "id BIGINT PRIMARY KEY AUTO_INCREMENT",
                "did VARCHAR(255)",
                "data_status VARCHAR(255)",
                "br_id VARCHAR(255)",
                "region VARCHAR(255)",
                "area VARCHAR(255)",
                "territory VARCHAR(255)",
                "respondent_name VARCHAR(255)",
                "cell_number VARCHAR(255)",
                "data_date VARCHAR(255)",
                "brand VARCHAR(255)",
                "target_selector VARCHAR(255)",
                "for_d VARCHAR(255)",
                "generate_status VARCHAR(255)",
                "is_called VARCHAR(255)",
                "createdAt TIMESTAMP"
        );

        String checkTableSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer tableCount = jdbcTemplate.queryForObject(checkTableSql, new Object[]{tableName}, Integer.class);

        if (tableCount != null && tableCount > 0) {
            String checkColumnsSql = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
            List<String> existingColumns = jdbcTemplate.queryForList(checkColumnsSql, new Object[]{tableName}, String.class);
            StringBuilder alterTableSql = new StringBuilder("ALTER TABLE " + tableName);
            boolean columnsExist = false;
            for (String column : columns) {
                if (existingColumns.contains(column)) {
                    columnsExist = true;
                } else {
                    alterTableSql.append(" ADD COLUMN ").append(column).append(" VARCHAR(255),");
                }
            }
            if (columnsExist) {
            } else {
                alterTableSql.setLength(alterTableSql.length() - 1);
                jdbcTemplate.execute(alterTableSql.toString());
            }
        } else {
            StringBuilder createTableSql = new StringBuilder("CREATE TABLE " + tableName + " (");

            for (String defaultColumn : defaultColumns) {
                createTableSql.append(defaultColumn).append(", ");
            }

            for (String column : columns) {
                createTableSql.append(column).append(" VARCHAR(255), ");
            }

            createTableSql.setLength(createTableSql.length() - 2);
            createTableSql.append(")");

            jdbcTemplate.execute(createTableSql.toString());
        }
    }

    @Transactional
    public void insertIntoTable(String tableName, Map<String, String> data) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        Object[] params = new Object[data.size()];
        int index = 0;

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (index > 0) {
                columns.append(", ");
                values.append(", ");
            }
            columns.append(entry.getKey());
            values.append("?");
            params[index++] = entry.getValue();
        }
        String insertSql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
        jdbcTemplate.update(insertSql, params);
    }

    public List<Map<String, Object>> queryTable(String tableName) {
        String querySql = "SELECT * FROM " + tableName;
        return jdbcTemplate.queryForList(querySql);
    }

    public byte[] writeTableColumnsToCsv(String tableName) throws SQLException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DatabaseMetaData metaData = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().getMetaData();
        try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
            try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
                writeColumnsToCsv(columns, writer);
            }
        }

        return out.toByteArray();
    }

    private void writeColumnsToCsv(ResultSet columns, CSVWriter writer) throws SQLException, IOException {
        List<String> columnNames = new ArrayList<>();
        while (columns.next()) {
            columnNames.add(columns.getString("COLUMN_NAME"));
        }
        writer.writeNext(columnNames.toArray(new String[0]));
    }

    private static final int BATCH_SIZE = 1000;

    public int uploadCsv(MultipartFile file, String tableName) throws IOException, SQLException, CsvException {
        int rowCount = 0;
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {
            List<String[]> records = csvReader.readAll();
            if (records.isEmpty()) {
                throw new IOException("CSV file is empty");
            }
            String[] columns = records.get(0);
            String insertQuery = buildInsertQuery(tableName, columns);
            List<String[]> dataRecords = records.subList(1, records.size());
            List<List<String[]>> batches = createBatches(dataRecords, BATCH_SIZE);
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            List<BatchUpdateTask> tasks = new ArrayList<>();
            for (List<String[]> batch : batches) {
                tasks.add(new BatchUpdateTask(jdbcTemplate, insertQuery, batch));
            }
            List<Future<Integer>> results = executorService.invokeAll(tasks);
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
            for (Future<Integer> result : results) {
                rowCount += result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Batch insertion interrupted", e);
        }

        return rowCount;
    }

    private String buildInsertQuery(String tableName, String[] columns) {
        String columnNames = String.join(", ", columns);
        String[] placeholders = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            placeholders[i] = "?";
        }
        String valuePlaceholders = String.join(", ", placeholders);
        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columnNames, valuePlaceholders);
    }

    private List<List<String[]>> createBatches(List<String[]> records, int batchSize) {
        List<List<String[]>> batches = new ArrayList<>();
        for (int i = 0; i < records.size(); i += batchSize) {
            int end = Math.min(records.size(), i + batchSize);
            batches.add(records.subList(i, end));
        }
        return batches;
    }

    public void updateLeadData(String campaignName, String cellNumber, Map<String, Object> updatedData) {
        if (updatedData == null || updatedData.isEmpty()) {
            throw new IllegalArgumentException("No data provided to update.");
        }
        StringBuilder sql = new StringBuilder("UPDATE ").append(campaignName).append(" SET ");
        for (String key : updatedData.keySet()) {
            sql.append(key).append(" = ?, ");
        }
        sql.append("createdAt = ?");
        sql.append(" WHERE cell_number = ?");
        Object[] values = new Object[updatedData.size() + 2]; // +2 for createdAt and cell_number
        int index = 0;
        for (Object value : updatedData.values()) {
            values[index++] = value;
        }
        values[index++] = Timestamp.valueOf(LocalDateTime.now());
        values[index] = cellNumber;
        try {
            jdbcTemplate.update(sql.toString(), values);
        } catch (DataAccessException e) {
            System.err.println("Error executing SQL: " + sql.toString());
            System.err.println("With values: " + Arrays.toString(values));
            System.err.println("Exception message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getNumberOfData(String campaignDataTable) {
        String sql = "SELECT COUNT(*) FROM " + campaignDataTable;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    public void dropCampaignTable(String campaignName) {
        String sql = "DROP table " + campaignName;
        jdbcTemplate.execute(sql);
    }

    private static class BatchUpdateTask implements Callable<Integer> {
        private final JdbcTemplate jdbcTemplate;
        private final String insertQuery;
        private final List<String[]> batch;

        public BatchUpdateTask(JdbcTemplate jdbcTemplate, String insertQuery, List<String[]> batch) {
            this.jdbcTemplate = jdbcTemplate;
            this.insertQuery = insertQuery;
            this.batch = batch;
        }

        @Override
        public Integer call() {
            List<Object[]> batchArgs = new ArrayList<>();
            for (String[] record : batch) {
                batchArgs.add(record);
            }
            int[] batchUpdateCounts = jdbcTemplate.batchUpdate(insertQuery, batchArgs);
            return batchUpdateCounts.length;
        }
    }

    public List<String> getUniqueDateDates(String tableName) {
        String sql = "SELECT DISTINCT data_date FROM " + tableName;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<String> getUniqueTargetSelector(String tableName) {
        String sql = "SELECT DISTINCT target_selector FROM " + tableName;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public void updateData(String tableName, Map<String, Integer> targetSelectors, String dataDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("WITH SortedData AS (")
                .append("    SELECT *, ")
                .append("           ROW_NUMBER() OVER (PARTITION BY br_id, target_selector ORDER BY ")
                .append("           CASE WHEN for_d != 'd' THEN 1 ELSE 2 END, ")
                .append("           CASE WHEN data_status = 'valid' THEN 1 ELSE 2 END, ")
                .append("           data_date) AS rn ")
                .append("    FROM ").append(tableName)
                .append(") ");
        sql.append("UPDATE ").append(tableName).append(" ")
                .append("SET for_d = 'd', ")
                .append("    generate_status = 1 ")
                .append("WHERE (br_id, id) IN (");
        sql.append("    SELECT br_id, id ")
                .append("    FROM SortedData ")
                .append("    WHERE data_date = ? ");
        if (!targetSelectors.isEmpty()) {
            sql.append("    AND (");
            int count = 0;
            for (Map.Entry<String, Integer> entry : targetSelectors.entrySet()) {
                if (count > 0) {
                    sql.append(" OR ");
                }
                String targetSelector = entry.getKey();
                Integer limit = entry.getValue();
                sql.append(" (target_selector = '").append(targetSelector).append("' AND rn <= ").append(limit).append(") ");
                count++;
            }
            sql.append("    )");
        }
        sql.append(")");
        jdbcTemplate.update(sql.toString(), dataDate);
    }

    public void reGenerate(String tableName, Map<String, Integer> targetSelectors, String dataDate) {
        String highestStatusQuery = "SELECT MAX(CAST(generate_status AS UNSIGNED)) FROM " + tableName;
        Integer highestStatus = jdbcTemplate.queryForObject(highestStatusQuery, Integer.class);
        if (highestStatus == null) {
            highestStatus = 0;
        }

        String countQuery = "SELECT br_id, target_selector, COUNT(*) AS count FROM " + tableName +
                " WHERE for_d = 'd' AND is_called = '' GROUP BY br_id, target_selector";
        List<Map<String, Object>> counts = jdbcTemplate.queryForList(countQuery);

        for (Map<String, Object> row : counts) {
            System.out.println("Row: " + row);
        }

        for (Map<String, Object> row : counts) {
            String brIdStr = (String) row.get("br_id");
            String targetSelector = (String) row.get("target_selector");

            Long brId = Long.valueOf(brIdStr);

            Integer existingCount = Integer.valueOf(row.get("count").toString()); // Convert count to Integer

            Integer requiredRows = targetSelectors.get(targetSelector);
            int rowsToRegenerate = requiredRows != null ? requiredRows : 0;

            rowsToRegenerate = Math.min(existingCount, rowsToRegenerate);
            if (rowsToRegenerate <= 0) {
                continue;
            }

            StringBuilder sql = new StringBuilder();
            sql.append("WITH SortedData AS (")
                    .append("    SELECT *, ")
                    .append("           ROW_NUMBER() OVER (PARTITION BY br_id, target_selector ORDER BY ")
                    .append("           CASE WHEN for_d != 'd' THEN 1 ELSE 2 END, ")
                    .append("           CASE WHEN data_status = 'valid' THEN 1 ELSE 2 END, ")
                    .append("           data_date) AS rowNum ")
                    .append("    FROM ").append(tableName)
                    .append("    WHERE br_id = ? AND target_selector = ? AND for_d = '' ")
                    .append(") ");

            sql.append("UPDATE ").append(tableName).append(" ")
                    .append("SET for_d = 'd', ")
                    .append("    generate_status = ? ")
                    .append("WHERE (br_id, id) IN (");
            sql.append("    SELECT br_id, id ")
                    .append("    FROM SortedData ")
                    .append("    WHERE data_date = ? ")
                    .append("    AND rowNum <= ?");
            sql.append(")");
            jdbcTemplate.update(sql.toString(), brId, targetSelector, highestStatus + 1, dataDate, rowsToRegenerate);
        }
    }

    @Transactional
    public void deleteLeads(String tableName, String dataDate) {
        String deleteQuery = "DELETE FROM " + tableName + " WHERE data_date = ?";
        jdbcTemplate.update(deleteQuery, dataDate);
    }

    @Transactional(readOnly = true)
    public byte[] generateExcel(String tableName, String dataDate) throws IOException {
        String query = "SELECT * FROM " + tableName + " WHERE for_d = 'd' AND data_date = ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query, dataDate);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Results");
        if (!results.isEmpty()) {
            Row headerRow = sheet.createRow(0);
            Map<String, Object> firstRow = results.get(0);
            int headerIndex = 0;
            for (String columnName : firstRow.keySet()) {
                Cell cell = headerRow.createCell(headerIndex++);
                cell.setCellValue(columnName);
            }
            int rowIndex = 1;
            for (Map<String, Object> row : results) {
                Row dataRow = sheet.createRow(rowIndex++);
                int cellIndex = 0;
                for (Object cellValue : row.values()) {
                    Cell cell = dataRow.createCell(cellIndex++);
                    if (cellValue != null) {
                        cell.setCellValue(cellValue.toString());
                    }
                }
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    @Transactional(readOnly = true)
    public byte[] generateFullReportInExcel(String tableName, String startDate, String endDate) throws IOException {
        String query = "SELECT * FROM " + tableName + " WHERE data_date BETWEEN ? AND ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query, startDate, endDate);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Results");
        if (!results.isEmpty()) {
            Row headerRow = sheet.createRow(0);
            Map<String, Object> firstRow = results.get(0);
            int headerIndex = 0;
            for (String columnName : firstRow.keySet()) {
                Cell cell = headerRow.createCell(headerIndex++);
                cell.setCellValue(columnName);
            }
            int rowIndex = 1;
            for (Map<String, Object> row : results) {
                Row dataRow = sheet.createRow(rowIndex++);
                int cellIndex = 0;
                for (Object cellValue : row.values()) {
                    Cell cell = dataRow.createCell(cellIndex++);
                    if (cellValue != null) {
                        cell.setCellValue(cellValue.toString());
                    }
                }
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    public List<CampaignDataDTO> getAllCampaignData() {
        List<String> tableNames = repository.findAllCampaignNames();
        List<CampaignDataDTO> allCampaignData = new ArrayList<>();
        for (String tableName : tableNames) {
            List<CampaignDataDTO> campaignData = getCampaignDataFromTable(tableName);
            allCampaignData.addAll(campaignData);
        }
        return allCampaignData;
    }

    public List<CampaignDataDTO> getCampaignDataFromTable(String tableName) {
        String query = String.format("SELECT COUNT(*) AS totalLeads, " +
                "(COUNT(*) - COALESCE(SUM(CASE WHEN generate_status = '' THEN 1 ELSE 0 END), 0)) AS generatedLeads, " +
                "COALESCE(SUM(CASE WHEN is_called = '1' THEN 1 ELSE 0 END), 0) AS calledLeads " +
                "FROM %s", tableName);
        Query nativeQuery = entityManager.createNativeQuery(query);
        List<Object[]> results = nativeQuery.getResultList();
        List<CampaignDataDTO> campaignDataList = new ArrayList<>();
        if (!results.isEmpty()) {
            Object[] row = results.get(0);
            int totalLeads = ((Number) row[0]).intValue();
            int generatedLeads = row[1] != null ? ((Number) row[1]).intValue() : 0;
            int calledLeads = row[2] != null ? ((Number) row[2]).intValue() : 0;
            CampaignDataDTO campaignData = new CampaignDataDTO(tableName, totalLeads, generatedLeads, calledLeads);
            campaignDataList.add(campaignData);
        }
        return campaignDataList;
    }
}
