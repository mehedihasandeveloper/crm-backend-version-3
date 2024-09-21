package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.service.DynamicTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dynamic")
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class DynamicTableController {

    @Autowired
    private DynamicTableService dynamicTableService;

    @PostMapping("/create")
    public void createTable(@RequestParam String tableName, @RequestParam List<String> columns) {
        dynamicTableService.createTable(tableName, columns);
    }

    @PostMapping("/insert")
    public void insertIntoTable(@RequestParam String tableName, @RequestBody Map<String, String> data) {
        dynamicTableService.insertIntoTable(tableName, data);
    }

    @GetMapping("/query")
    public List<Map<String, Object>> queryTable(@RequestParam String tableName) {
        return dynamicTableService.queryTable(tableName);
    }

    @GetMapping("/unique_data_dates/{tableName}")
    public List<String> getUniqueJoiningDates(@PathVariable String tableName) {
        return dynamicTableService.getUniqueDateDates(tableName);
    }

    @GetMapping("/unique_target_selector/{tableName}")
    public List<String> getUniqueTargetSelector(@PathVariable String tableName) {
        return dynamicTableService.getUniqueTargetSelector(tableName);
    }


    @PostMapping("/updateData")
    public ResponseEntity<Map<String, String>> updateData(
            @RequestParam String tableName,
            @RequestParam String dataDate,
            @RequestBody Map<String, Integer> callTargets) {
        try {
            dynamicTableService.updateData(tableName, callTargets, dataDate);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Data updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error updating data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


//    @PostMapping("/reGenerateData")
//    public void reGenerate(String tableName, String dataDate) {
//        dynamicTableService.reGenerateData(tableName, dataDate);
//    }

    @PostMapping("/reGenerateData")
    public void reGenerate(
            @RequestParam String tableName,
            @RequestParam String dataDate,
            @RequestBody Map<String, Integer> targetSelectors) {
        dynamicTableService.reGenerate(tableName, targetSelectors, dataDate);
    }

    @DeleteMapping("/deleteLead")
    public void deleteLeads(String tableName, String dataDate) {
        dynamicTableService.deleteLeads(tableName, dataDate);
    }

    @PutMapping("/updateLeadData/{campaignName}/{cellNumber}")
    public void updateLeadData(@PathVariable String campaignName,
                               @PathVariable String cellNumber,
                               @RequestBody(required = false) Map<String, Object> updatedData) {
        System.out.println("Received Data: " + updatedData); // Log received data
        if (updatedData == null) {
            System.out.println("Updated Data is null");
        }
        dynamicTableService.updateLeadData(campaignName, cellNumber, updatedData);
    }

    @GetMapping("/countNumberOfDataAvailable")
    public int countNumberOfDataInTable(String campaignDataTable){
       return dynamicTableService.getNumberOfData(campaignDataTable);
    }

    @DeleteMapping("/deleteCampaignTable")
    public void deleteCampaignTable(String campaignName){
        dynamicTableService.dropCampaignTable(campaignName);
    }
}
