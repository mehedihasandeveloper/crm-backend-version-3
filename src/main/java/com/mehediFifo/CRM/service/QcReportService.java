package com.mehediFifo.CRM.service;


import com.mehediFifo.CRM.DTO.AgentSummary;
import com.mehediFifo.CRM.DTO.DateRangeSummary;
import com.mehediFifo.CRM.entity.Agent;
import com.mehediFifo.CRM.entity.QcReport;
import com.mehediFifo.CRM.entity.QcReportClient;
import com.mehediFifo.CRM.repository.AgentRepository;
import com.mehediFifo.CRM.repository.QcReportClientRepo;
import com.mehediFifo.CRM.repository.QcReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import java.util.stream.Collectors;

@Service
public class QcReportService {
    @Autowired
    private QcReportRepository repository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private QcReportClientRepo clientRepository;

    public synchronized QcReport addQcReport(QcReport qcReport) {

        Optional<Agent> agentOpt = agentRepository.findByAgentId(qcReport.getAgentId());

        if (agentOpt.isPresent()) {
            Agent agent = agentOpt.get();
            qcReport.setAgentName(agent.getName());
        } else {
            throw new RuntimeException("Agent not found with agentId: " + qcReport.getAgentId());
        }

        return repository.save(qcReport);
    }

    public synchronized QcReportClient addQcReportClient(QcReportClient qcReport) {

        Optional<Agent> agentOpt = agentRepository.findByAgentId(qcReport.getAgentId());

        if (agentOpt.isPresent()) {
            Agent agent = agentOpt.get();
            qcReport.setAgentName(agent.getName());
        } else {
            throw new RuntimeException("Agent not found with agentId: " + qcReport.getAgentId());
        }

        return clientRepository.save(qcReport);
    }


    public Page<QcReport> getAllQcReportByQcInspectorWithPagination(String username, int offset, int pageSize) {
        return repository.findAllByQcInspector(username, PageRequest.of(offset, pageSize));
    }

    public Page<QcReportClient> getAllQcRecordsClientByQcInspectorWithPagination(String username, int offset, int pageSize) {
        return clientRepository.findAllByQcInspector(username, PageRequest.of(offset, pageSize));
    }

    public Page<QcReport> getAllQcReportWithPagination(int offset, int pageSize) {
        return repository.findAll(PageRequest.of(offset, pageSize));
    }

    public void removeQc(Long id) {
        repository.deleteById(id);
    }

    public void removeClientQc(Long id) {
        clientRepository.deleteById(id);
    }

    public QcReport getQcById(Long id) {
        return repository.findById(id).get();
    }

    public QcReport updateQcReport(QcReport qcReport) {
        QcReport existingQcReport = repository.findById(qcReport.getId()).orElse(qcReport);
        existingQcReport.setGreetings(qcReport.getGreetings());
        existingQcReport.setLiveliness(qcReport.getLiveliness());
        existingQcReport.setPronunciation(qcReport.getPronunciation());
        existingQcReport.setMumbling(qcReport.getMumbling());
        existingQcReport.setPace(qcReport.getPace());
        existingQcReport.setPitch(qcReport.getPitch());
        existingQcReport.setCourtesy(qcReport.getCourtesy());
        existingQcReport.setHoldProcess(qcReport.getHoldProcess());
        existingQcReport.setTakingPermission(qcReport.getTakingPermission());
        existingQcReport.setAcknowledgementAndFollowUp(qcReport.getAcknowledgementAndFollowUp());
        existingQcReport.setPoorObjectionAndNegotiationSkill(qcReport.getPoorObjectionAndNegotiationSkill());
        existingQcReport.setCrm(qcReport.getCrm());
        existingQcReport.setClosing(qcReport.getClosing());
        existingQcReport.setFatal(qcReport.getFatal());
        existingQcReport.setFatalReason(qcReport.getFatalReason());
        existingQcReport.setEasVoiceMatchedWithReport(qcReport.getEasVoiceMatchedWithReport());
        existingQcReport.setTotal(qcReport.getTotal());
        existingQcReport.setAgentGrade(qcReport.getAgentGrade());
        existingQcReport.setSuggestion(qcReport.getSuggestion());
        return repository.save(existingQcReport);
    }


    public QcReportClient updateClientQcReport(QcReportClient qcReportClient) {
        QcReportClient existingClientQcReport = clientRepository.findById(qcReportClient.getId()).orElse(qcReportClient);
        existingClientQcReport.setGreetingsMarks(qcReportClient.getGreetingsMarks());
        existingClientQcReport.setLivelinessMarks(qcReportClient.getLivelinessMarks());
        existingClientQcReport.setPronunciationMarks(qcReportClient.getPronunciationMarks());
        existingClientQcReport.setMumblingMarks(qcReportClient.getMumblingMarks());
        existingClientQcReport.setPaceMarks(qcReportClient.getPaceMarks());
        existingClientQcReport.setPitchMarks(qcReportClient.getPitchMarks());
        existingClientQcReport.setCourtesyMarks(qcReportClient.getCourtesyMarks());
        existingClientQcReport.setHoldProcessMarks(qcReportClient.getHoldProcessMarks());
        existingClientQcReport.setTakingPermissionMarks(qcReportClient.getTakingPermissionMarks());
        existingClientQcReport.setAcknowledgementAndFollowUpMarks(qcReportClient.getAcknowledgementAndFollowUpMarks());
        existingClientQcReport.setPoorObjectionAndNegotiationSkillMarks(qcReportClient.getPoorObjectionAndNegotiationSkillMarks());
        existingClientQcReport.setCrmMarks(qcReportClient.getCrmMarks());
        existingClientQcReport.setClosingMarks(qcReportClient.getClosingMarks());
        existingClientQcReport.setFatalMarks(qcReportClient.getFatalMarks());
        existingClientQcReport.setFatalReasonMarks(qcReportClient.getFatalReasonMarks());
        existingClientQcReport.setEasVoiceMatchedWithReportMarks(qcReportClient.getEasVoiceMatchedWithReportMarks());
        existingClientQcReport.setTotal(qcReportClient.getTotal());
        existingClientQcReport.setAgentGrade(qcReportClient.getAgentGrade());
        existingClientQcReport.setSuggestionMarks(qcReportClient.getSuggestionMarks());
        return clientRepository.save(existingClientQcReport);
    }

    public QcReportClient getClientQcRecordById(Long id) {
        return clientRepository.findById(id).get();
    }


    public List<Map<String, Object>> getAllQcRecordsInHouse(String date) {
        List<QcReport> reports = repository.findAllByCallDate(date);

        return reports.stream().map(report -> {
            Map<String, Object> modifiedReport = new HashMap<>();

            // Add unchanged fields
            modifiedReport.put("callDate", report.getCallDate());
            modifiedReport.put("agentName", report.getAgentName());
            modifiedReport.put("agentId", report.getAgentId());
            modifiedReport.put("campaignName", report.getCampaignName());
            modifiedReport.put("consumerNumber", report.getConsumerNumber());
            modifiedReport.put("duration", report.getDuration());
            // Add modified fields
            modifiedReport.put("Greetings ( শুভেচ্ছা ) [Yes(0)/No(-5)]", report.getGreetings());
            modifiedReport.put("Liveliness(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getLiveliness());
            modifiedReport.put("Pronunciation(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getPronunciation());
            modifiedReport.put("Mumbling(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getMumbling());
            modifiedReport.put("Pace(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getPace());
            modifiedReport.put("Pitch(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getPitch());
            modifiedReport.put("Courtesy(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getCourtesy());
            modifiedReport.put("Hold Process(0) [Yes(0)/No(-5)]", report.getHoldProcess());
            modifiedReport.put("Taking Permission (10) [Yes(10)/ No(0)]", report.getTakingPermission());
            modifiedReport.put("Acknowledgement and follow-up(10) [Yes(10)/ No(0)]", report.getAcknowledgementAndFollowUp());
            modifiedReport.put("Poor objection & Negotiation skill(-20) [Yes(0.00)/Need Improvement(-10)/ No(-20.00)]", report.getPoorObjectionAndNegotiationSkill());
            modifiedReport.put("CRM(20) [Yes(20)/Need Improvement(10)/No(0)]", report.getCrm());
            modifiedReport.put("Closing [Yes(0)/ No(-5)]", report.getClosing());
            modifiedReport.put("Fatal [Yes(-100)/ No(0)]", report.getFatal());
            modifiedReport.put("fatalReason", report.getFatalReason());
            modifiedReport.put("easVoiceMatchedWithReport", report.getEasVoiceMatchedWithReport());
            modifiedReport.put("total", report.getTotal());
            modifiedReport.put("agentGrade", report.getAgentGrade());
            modifiedReport.put("suggestion", report.getSuggestion());
            modifiedReport.put("qcInspector", report.getQcInspector());
            return modifiedReport;
        }).collect(Collectors.toList());
    }

    public List<String> getUniqueDatesInHouseForReporting() {
        return repository.findDistinctCallDate();
    }

    public List<String> getUniqueDatesClientForReporting() {
        return clientRepository.findDistinctCallDate();
    }

    public List<Map<String, Object>> getAllQcRecordsClient(String date) {
        List<QcReportClient> reports = clientRepository.findAllByCallDate(date);

        return reports.stream().map(report -> {
            Map<String, Object> modifiedReport = new HashMap<>();
            // Add unchanged fields
            modifiedReport.put("callDate", report.getCallDate());
            modifiedReport.put("agentName", report.getAgentName());
            modifiedReport.put("agentId", report.getAgentId());
            modifiedReport.put("campaignName", report.getCampaignName());
            modifiedReport.put("consumerNumber", report.getConsumerNumber());
            modifiedReport.put("duration", report.getDuration());
            // Add modified fields
            modifiedReport.put("Greetings ( শুভেচ্ছা ) [Yes(0)/No(-5)]", report.getGreetingsMarks());
            modifiedReport.put("Liveliness(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getLivelinessMarks());
            modifiedReport.put("Pronunciation(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getPronunciationMarks());
            modifiedReport.put("Mumbling(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getMumblingMarks());
            modifiedReport.put("Pace(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getPaceMarks());
            modifiedReport.put("Pitch(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getPitchMarks());
            modifiedReport.put("Courtesy(10) [Yes(10)/Need Improvement(5)/ No(0)]", report.getCourtesyMarks());
            modifiedReport.put("Hold Process(0) [Yes(0)/No(-5)]", report.getHoldProcessMarks());
            modifiedReport.put("Taking Permission (10) [Yes(10)/ No(0)]", report.getTakingPermissionMarks());
            modifiedReport.put("Acknowledgement and follow-up(10) [Yes(10)/ No(0)]", report.getAcknowledgementAndFollowUpMarks());
            modifiedReport.put("Poor objection & Negotiation skill(-20) [Yes(0.00)/Need Improvement(-10)/ No(-20.00)]", report.getPoorObjectionAndNegotiationSkillMarks());
            modifiedReport.put("CRM(20) [Yes(20)/Need Improvement(10)/No(0)]", report.getCrmMarks());
            modifiedReport.put("Closing [Yes(0)/ No(-5)]", report.getClosingMarks());
            modifiedReport.put("Fatal [Yes(-100)/ No(0)]", report.getFatalMarks());
            modifiedReport.put("fatalReason", report.getFatalReasonMarks());
            modifiedReport.put("easVoiceMatchedWithReport", report.getEasVoiceMatchedWithReportMarks());
            modifiedReport.put("total", report.getTotal());
            modifiedReport.put("agentGrade", report.getAgentGrade());
            modifiedReport.put("suggestion", report.getSuggestionMarks());
            modifiedReport.put("qcInspector", report.getQcInspector());
            return modifiedReport;
        }).collect(Collectors.toList());
    }

    public List<AgentSummary> getAgentSummaries(String callDate) {
        // Fetch all reports by the provided call date
        List<QcReport> reports = repository.findAllByCallDate(callDate);

        // Group reports by agentId
        Map<String, List<QcReport>> groupedReports = reports.stream()
                .filter(report -> report.getAgentId() != null) // Filter out any null agentId entries
                .collect(Collectors.groupingBy(QcReport::getAgentId));

        // Process each agent’s records to compute the average and classify color
        return groupedReports.entrySet().stream().map(entry -> {
            List<QcReport> agentReports = entry.getValue();
            String agentId = entry.getKey();
            String agentName = agentReports.get(0).getAgentName();

            // Calculate the average score, handling null values in total
            OptionalDouble averageScoreOpt = agentReports.stream()
                    .mapToInt(report -> report.getTotal() != null ? report.getTotal() : 0)
                    .average();

            double averageScore = averageScoreOpt.orElse(0);

            // Determine the color category based on the average score
            String red = "", yellow = "", blue = "", green = "";
            if (averageScore < 70) {
                red = String.valueOf(averageScore);
            } else if (averageScore < 80) {
                yellow = String.valueOf(averageScore);
            } else if (averageScore < 90) {
                blue = String.valueOf(averageScore);
            } else {
                green = String.valueOf(averageScore);
            }

            // Aggregate all suggestions into a comma-separated string
            String suggestions = agentReports.stream()
                    .map(QcReport::getSuggestion)
                    .filter(suggestion -> suggestion != null && !suggestion.isEmpty())
                    .collect(Collectors.joining(", "));

            // Create and return the summary for each agent
            return new AgentSummary(agentName, agentId, red, yellow, blue, green, suggestions);
        }).collect(Collectors.toList());
    }

    public List<DateRangeSummary> getSummaryWithinDateRange(LocalDate startDate, LocalDate endDate) {
        List<QcReport> reports = repository.findByCallDateBetween(startDate.toString(), endDate.toString());

        // Group reports by agentId and then by date
        Map<String, Map<String, List<QcReport>>> groupedReports = reports.stream()
                .collect(Collectors.groupingBy(QcReport::getAgentId,
                        Collectors.groupingBy(QcReport::getCallDate)));

        List<DateRangeSummary> summaries = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<QcReport>>> agentEntry : groupedReports.entrySet()) {
            String agentId = agentEntry.getKey();
            Map<String, List<QcReport>> dateReports = agentEntry.getValue();

            DateRangeSummary summary = new DateRangeSummary();
            summary.setAgentId(agentId);
            summary.setAgentName(dateReports.values().stream().findFirst().get().get(0).getAgentName());

            Map<String, Double> dailyAverages = new LinkedHashMap<>();
            double sum = 0;
            long count = 0;

            // Iterate over each date in the range
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                String dateStr = currentDate.toString();
                List<QcReport> dailyReports = dateReports.get(dateStr);

                if (dailyReports != null && !dailyReports.isEmpty()) {
                    double dailyTotal = dailyReports.stream()
                            .mapToInt(QcReport::getTotal)
                            .average()
                            .orElse(0);

                    dailyAverages.put(dateStr, dailyTotal);
                    sum += dailyTotal;
                    count++;
                } else {
                    dailyAverages.put(dateStr, null); // No data for this date
                }
                currentDate = currentDate.plusDays(1);
            }

            summary.setDailyAverages(dailyAverages);
            summary.setSum(sum);
            summary.setCount(count);
            summary.setAverage(count > 0 ? sum / count : 0);

            summaries.add(summary);
        }

        return summaries;
    }

    public List<QcReport> getAllQcRecordsByAgentId(String agentId) {
        return repository.findByAgentId(agentId);
    }
}
