package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.DTO.AgentSummary;
import com.mehediFifo.CRM.DTO.DateRangeSummary;
import com.mehediFifo.CRM.config.APIResponse;
import com.mehediFifo.CRM.entity.AgentReview;
import com.mehediFifo.CRM.entity.QcReport;
import com.mehediFifo.CRM.entity.QcReportClient;
import com.mehediFifo.CRM.entity.QcReportFile;
import com.mehediFifo.CRM.repository.AgentReviewRepository;
import com.mehediFifo.CRM.service.QcReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/qcReport")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class QcReportController {

    @Autowired
    private QcReportService service;

    @Autowired
    private AgentReviewRepository reviewRepository;

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
    @GetMapping("/get-all-qc-records-in-house-for-excel-print")
    public List<Map<String, Object>> getAllQcReportInHouse(String date) {
        return service.getAllQcRecordsInHouse(date);
    }

    // get all Client QC records for Excel report
    @GetMapping("/get-all-qc-records-client-for-excel-print")
    public List<Map<String, Object>> getAllQcReportClient(String date) {
        return service.getAllQcRecordsClient(date);
    }


    // get distinct dates for downloading reports for In-house need

    @GetMapping("/unique-dates-download-reports-in-house")
    public List<String> getUniqueDatesInHouse(){
        return service.getUniqueDatesInHouseForReporting();
    }

    // get distinct dates for downloading reports for client need

    @GetMapping("/unique-dates-download-reports-client")
    public List<String> getUniqueDatesClient(){
        return service.getUniqueDatesClientForReporting();
    }

    @GetMapping("/qc-report/summary")
    public List<AgentSummary> getAgentSummaries(@RequestParam String callDate) {
        return service.getAgentSummaries(callDate);
    }

    @GetMapping("/date-range-summary")
    public List<DateRangeSummary> getDateRangeSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return service.getSummaryWithinDateRange(startDate, endDate);
    }

    // get qc records by agentId
//    @GetMapping("list-of-qc-records-by-agent-id")
//    public List<QcReport> getAllByAgentId(String agentId){
//        return service.getAllQcRecordsByAgentId(agentId);
//    }

    @GetMapping("list-of-qc-records-by-agent-id")
    public List<QcReport> getAllByAgentIdExcludingReviewed(@RequestParam String agentId) {
        return service.getAllQcRecordsByAgentIdExcludingReviewed(agentId);
    }

// add file path name
    @PostMapping("/add-qc-report-file")
    public QcReportFile addReportFile(@RequestBody QcReportFile reportFile) {
        return service.save(reportFile);
    }

    // get file path name
    @GetMapping("/all-qc-report-file")
    public List<QcReportFile> getAllReportFiles() {
        return service.getAllReportFiles();
    }

    @PostMapping("/submit")
    public ResponseEntity<AgentReview> submitReview(@RequestBody AgentReview review) {
        AgentReview savedReview = reviewRepository.save(review);
        return ResponseEntity.ok(savedReview);
    }

}
