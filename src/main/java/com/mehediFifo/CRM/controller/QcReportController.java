package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.config.APIResponse;
import com.mehediFifo.CRM.entity.Agent;
import com.mehediFifo.CRM.entity.QcReport;
import com.mehediFifo.CRM.service.QcReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/qcReport")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class QcReportController {

    @Autowired
    private QcReportService service;

    @PostMapping("/add")
    public QcReport addQC(@RequestBody QcReport qcReport) {
        return service.addQcReport(qcReport);
    }

    @GetMapping("/getqcreportbyqcinspector/{offset}/{pageSize}")
    public APIResponse<Page<QcReport>> getQcReportByQcInspectorWithPagination(
            @RequestParam String username,
            @PathVariable int offset,
            @PathVariable int pageSize) {
        Page<QcReport> qcReportWithPagination = service.getAllQcReportByQcInspectorWithPagination(username, offset, pageSize);
        return new APIResponse<>(qcReportWithPagination.getSize(), qcReportWithPagination);
    }


    @GetMapping("/getallqcreports/{offset}/{pageSize}")
    public APIResponse<Page<QcReport>> getAllQcReportsWithPagination( @PathVariable int offset,
                                           @PathVariable int pageSize) {
        Page<QcReport> qcReportWithPagination = service.getAllQcReportWithPagination(offset, pageSize);
        return new APIResponse<>(qcReportWithPagination.getSize(), qcReportWithPagination);
    }

    @DeleteMapping("/deleteQc/{id}")
    public void deleteQc(@PathVariable Long id){
       service.removeQc(id);
    }

    @GetMapping("/getqc/{id}")
    public QcReport getQcById(@PathVariable Long id){
        return service.getQcById(id);
    }

    @PutMapping("/editqc")
    public QcReport editQc(@RequestBody QcReport qcReport){
        return service.updateQcReport(qcReport);
    }
}
