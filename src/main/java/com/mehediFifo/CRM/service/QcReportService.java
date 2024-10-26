package com.mehediFifo.CRM.service;

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

import java.util.List;
import java.util.Optional;

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

    public List<QcReport> getAllQcRecords(String date) {
        return repository.findAllByCallDate(date);
    }
}
