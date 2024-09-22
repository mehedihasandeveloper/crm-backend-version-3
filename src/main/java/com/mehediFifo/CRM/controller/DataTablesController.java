package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.config.APIResponse;
import com.mehediFifo.CRM.entity.DataTables;
import com.mehediFifo.CRM.service.DataTablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dt")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class DataTablesController {
    @Autowired
    private DataTablesService service;

    // Adding new data tables
    @PostMapping("/addDataTables")
    public DataTables addDataTables(@RequestBody DataTables dataTables){
        return service.saveDataTables(dataTables);
    }

    // Calling all campaigns
    @GetMapping("/viewAllDataTables")
    public List<DataTables> findAllCampaigns() {
        return service.getAllDataTables();
    }

    // Deleting data tables
    @DeleteMapping("/deleteDataTables/{id}")
    public void deleteDataTables(@PathVariable Long id){
        service.deleteDataTables(id);
    }

    @GetMapping("/searchDataTables")
    public List<DataTables> searchDataTables(@RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "campaignName", required = false) String campaignName,
                                         @RequestParam(value = "numberOfField", required = false) Integer numberOfField) {
        return service.search(name, campaignName, numberOfField);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public APIResponse<Page<DataTables>> getDataTablesListWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<DataTables> DataTablesWithPagination = service.findDataTablesWithPagination(offset, pageSize);
        return new APIResponse<>(DataTablesWithPagination.getSize(), DataTablesWithPagination);
    }

    @GetMapping("/totalDataTables")
    public Integer getTotalDataTables() {
        return service.getTotalDataTables();
    }
}
