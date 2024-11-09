package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.service.DynamicTableService;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/file")
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class CsvController {

    @Autowired
    private DynamicTableService dynamicTableService;

    @GetMapping("/download/csv")
    public ResponseEntity<byte[]> downloadCSV() {
        String[] columns = {"name", "type", "agentId", "joiningDate", "cellNumber", "bankAcNumber", "nagadAcNumber", "bkashAcNumber"};
        String[][] data = {
                {"John Doe", "Physical", "420", "01/01/2024", "01925111043", "100100100", "200200200", "300300300"},
                {"Doe John", "Virtual", "421", "01/01/2024", "01925111043", "100100100", "200200200", "300300300"}
        };

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(out);

        // Write columns
        pw.println(String.join(",", columns));

        // Write data
        for (String[] row : data) {
            pw.println(String.join(",", row));
        }

        pw.flush();
        byte[] csvData = out.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(csvData);
    }

    @GetMapping(value = "/download/columns/csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadColumnNamesCSV(@RequestParam String tableName) {
        try {
            byte[] csvData = dynamicTableService.writeTableColumnsToCsv(tableName);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", tableName + "_columns.csv");
            headers.setContentType(MediaType.parseMediaType("text/csv"));

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(csvData);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body(null);
        }
    }

    @PostMapping(value = "uploadCsv", consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file,
                                            @RequestParam("tableName") String tableName) {
        try {
            int insertedRows = dynamicTableService.uploadCsv(file, tableName);
            String responseMessage = String.format("File uploaded and %d rows inserted successfully", insertedRows);
            return ResponseEntity.ok(responseMessage);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file");
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

//    @GetMapping("/generateExcel")
//    public ResponseEntity<byte[]> generateExcel(@RequestParam String tableName,
//                                                @RequestParam String dataDate) {
//        try {
//            byte[] excelData = dynamicTableService.generateExcel(tableName, dataDate);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_for_d.xlsx");
//            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//
//            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/generateCsv")
    public ResponseEntity<byte[]> generateCsv(@RequestParam String tableName,
                                              @RequestParam String dataDate) {
        try {
            byte[] csvData = dynamicTableService.generateCsv(tableName, dataDate);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_for_d.csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

            return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping("/generateFullReportExcel")
//    public ResponseEntity<byte[]> generateExcel(@RequestParam String tableName,
//                                                @RequestParam String startDate,
//                                                @RequestParam String endDate) {
//        try {
//            byte[] excelData = dynamicTableService.generateFullReportInExcel(tableName, startDate, endDate);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=full_report.xlsx");
//            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//
//            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/generateFullReportCsv")
    public ResponseEntity<byte[]> generateCsv(@RequestParam String tableName,
                                              @RequestParam String startDate,
                                              @RequestParam String endDate) {
        try {
            byte[] csvData = dynamicTableService.generateFullReportInCsv(tableName, startDate, endDate);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=full_report.csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

            return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
