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


    public Page<QcReport> getAllQcReportWithPagination(int offset, int pageSize) {
        return repository.findAll(PageRequest.of(offset, pageSize));
    }

    public void removeQc(Long id) {
        repository.deleteById(id);
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
}
