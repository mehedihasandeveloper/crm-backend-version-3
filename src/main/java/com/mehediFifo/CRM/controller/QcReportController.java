package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.config.APIResponse;
import com.mehediFifo.CRM.entity.QcReport;
import com.mehediFifo.CRM.entity.QcReportClient;
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

    @PostMapping("/addforclient")
    public QcReportClient addQC(@RequestBody QcReportClient qcReport) {
        return service.addQcReportClient(qcReport);
    }

    @GetMapping("/getqcreportbyqcinspector/{offset}/{pageSize}")
    public APIResponse<Page<QcReport>> getQcReportByQcInspectorWithPagination(
            @RequestParam String username,
            @PathVariable int offset,
            @PathVariable int pageSize) {
        Page<QcReport> qcReportWithPagination = service.getAllQcReportByQcInspectorWithPagination(username, offset, pageSize);
        return new APIResponse<>(qcReportWithPagination.getSize(), qcReportWithPagination);
    }

    // Qc Records Client view by user
    @GetMapping("/getQcRecordsClientViewByQcInspector/{offset}/{pageSize}")
    public APIResponse<Page<QcReportClient>> getQcRecordsClientView(
            @RequestParam String username,
            @PathVariable int offset,
            @PathVariable int pageSize) {
        Page<QcReportClient> qcRecordsWithPagination = service.getAllQcRecordsClientByQcInspectorWithPagination(username, offset, pageSize);
        return new APIResponse<>(qcRecordsWithPagination.getSize(), qcRecordsWithPagination);
    }

    @GetMapping("/getallqcreports/{offset}/{pageSize}")
    public APIResponse<Page<QcReport>> getAllQcReportsWithPagination(@PathVariable int offset,
                                                                     @PathVariable int pageSize) {
        Page<QcReport> qcReportWithPagination = service.getAllQcReportWithPagination(offset, pageSize);
        return new APIResponse<>(qcReportWithPagination.getSize(), qcReportWithPagination);
    }


    @DeleteMapping("/deleteQc/{id}")
    public void deleteQc(@PathVariable Long id) {
        service.removeQc(id);
    }

    // client qc delete
    @DeleteMapping("/deleteClientQc/{id}")
    public void deleteClientQc(@PathVariable Long id) {
        service.removeClientQc(id);
    }

    @GetMapping("/getqc/{id}")
    public QcReport getQcById(@PathVariable Long id) {
        return service.getQcById(id);
    }

    @PutMapping("/editqc")
    public QcReport editQc(@RequestBody QcReport qcReport) {
        return service.updateQcReport(qcReport);
    }

    @PutMapping("/edit-Client-Qc")
    public QcReportClient editClientQc(@RequestBody QcReportClient qcReportClient) {
        return service.updateClientQcReport(qcReportClient);
    }

    // get client qc record
    @GetMapping("/getClientQc/{id}")
    public QcReportClient getQcClientRecordById(@PathVariable Long id){
        return service.getClientQcRecordById(id);
    }

    // get all In-house QC records for Excel report
    @GetMapping("/get-all-qc-records-inhouse-for-excel-print")
    public List<QcReport> getAllQcReport(String date){
        return service.getAllQcRecords(date);
    }

    

}
