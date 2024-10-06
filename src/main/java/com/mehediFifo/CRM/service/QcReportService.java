package com.mehediFifo.CRM.service;

import com.mehediFifo.CRM.entity.Agent;
import com.mehediFifo.CRM.entity.QcReport;
import com.mehediFifo.CRM.repository.AgentRepository;
import com.mehediFifo.CRM.repository.QcReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QcReportService {
    @Autowired
    private QcReportRepository repository;

    @Autowired
    private AgentRepository agentRepository;

    public synchronized QcReport addQcReport(QcReport qcReport){

        Optional<Agent> agentOpt = agentRepository.findByAgentId(qcReport.getAgentId());

        if (agentOpt.isPresent()) {
            Agent agent = agentOpt.get();
            qcReport.setAgentName(agent.getName());
        } else {
            throw new RuntimeException("Agent not found with agentId: " + qcReport.getAgentId());
        }

        return repository.save(qcReport);
    }
}
