package com.mehediFifo.CRM.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgentPanelService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getObjectByNumber(String tableName, String cellNumber) {
        String querySql = "SELECT * FROM " + tableName + " WHERE cell_number = ? AND for_d = 'd'";

        List<Map<String, Object>> result = jdbcTemplate.query(querySql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> map = new HashMap<>();
                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                return map;
            }
        }, cellNumber);

        if (result.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lead is not generated");
            return errorResponse;
        }

        // Return the first result if found (or handle as needed)
        return result.get(0);
    }



//    public Object getObjectByNumber(String tableName, String cellNumber) {
//        String querySql = "SELECT * FROM " + tableName + " WHERE cell_number = ? AND for_d = 'd'";
//
//        List<Map<String, Object>> result = jdbcTemplate.query(querySql, new RowMapper<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Map<String, Object> map = new HashMap<>();
//                int columnCount = rs.getMetaData().getColumnCount();
//                for (int i = 1; i <= columnCount; i++) {
//                    map.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
//                }
//                return map;
//            }
//        }, cellNumber);
//
//        if (result.isEmpty()) {
//            return "Lead is not generated";
//        }
//
//        // Return the first result if found (or handle as needed)
//        return result.get(0);
//    }



//    public Map<String, Object> getObjectByNumber(String tableName, String cellNumber) {
//        String querySql = "SELECT * FROM " + tableName + " WHERE cell_number = ?";
//        return jdbcTemplate.queryForObject(querySql, new RowMapper<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Map<String, Object> map = new HashMap<>();
//                int columnCount = rs.getMetaData().getColumnCount();
//                for (int i = 1; i <= columnCount; i++) {
//                    map.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
//                }
//                return map;
//            }
//        }, cellNumber);
//    }


}
