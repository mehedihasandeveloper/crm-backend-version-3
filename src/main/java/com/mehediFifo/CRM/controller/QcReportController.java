package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.entity.QcReport;
import com.mehediFifo.CRM.service.QcReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qcReport")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class QcReportController {

    @Autowired
    private QcReportService service;

    @PostMapping("/add")
    public QcReport addQC(@RequestBody QcReport qcReport){
        return service.addQcReport(qcReport);
    }
}
