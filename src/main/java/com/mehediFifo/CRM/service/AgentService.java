package com.mehediFifo.CRM.service;

import com.mehediFifo.CRM.customException.InvalidDataException;
import com.mehediFifo.CRM.entity.Agent;
import com.mehediFifo.CRM.entity.Statistics;
import com.mehediFifo.CRM.repository.AgentRepository;
import com.mehediFifo.CRM.repository.StatisticsRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);

    @Autowired
    private AgentRepository repository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    public synchronized Agent addAgent(Agent agent) {
        Agent savedAgent = repository.save(agent);
        Statistics stats = statisticsRepository.findById(1L).orElse(new Statistics(0, 0));
        stats.setTotalAgents(stats.getTotalAgents() + 1);
        statisticsRepository.save(stats);
        return savedAgent;
    }



    public List<Agent> getAgentList() {
        return repository.findAll();
    }

    public void removeAgent(Long id) {
        repository.deleteById(id);

        Statistics stats = statisticsRepository.findById(1L).orElse(new Statistics(0, 0));
        stats.setTotalAgents(stats.getTotalAgents() - 1);
        statisticsRepository.save(stats);
    }

    public synchronized Agent editAgent(Agent agent) {
        Agent existingAgent = repository.findById(agent.getId()).orElse(agent);
        existingAgent.setName(agent.getName());
        existingAgent.setType(agent.getType());
        existingAgent.setAgentId(agent.getAgentId());
        existingAgent.setCellNumber(agent.getCellNumber());
        existingAgent.setJoiningDate(agent.getJoiningDate());
        existingAgent.setBankAcNumber(agent.getBankAcNumber());
        existingAgent.setNagadAcNumber(agent.getNagadAcNumber());
        existingAgent.setBkashAcNumber(agent.getBkashAcNumber());
        return repository.save(existingAgent);
    }

    public Agent findAgent(Long id) {
        return repository.findById(id).orElse(null);
    }


    public synchronized Integer uploadAgents(MultipartFile file) throws IOException, InvalidDataException {
        Set<Agent> agents = parseCsv(file);
        repository.saveAll(agents);

        // Update totalAgents in AgentStatistics
        Statistics stats = statisticsRepository.findById(1L).orElse(new Statistics(0, 0));
        stats.setTotalAgents(stats.getTotalAgents() + agents.size());
        statisticsRepository.save(stats);
        return agents.size();
    }

    private synchronized Set<Agent> parseCsv(MultipartFile file) throws IOException, InvalidDataException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<agentCsvRepresentation> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(agentCsvRepresentation.class);
            CsvToBean<agentCsvRepresentation> csvToBean = new CsvToBeanBuilder<agentCsvRepresentation>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<agentCsvRepresentation> csvLines = csvToBean.parse();
            for (agentCsvRepresentation csvLine : csvLines) {
                if (isAnyFieldMissing(csvLine)) {
                    throw new InvalidDataException("Request failed! Check if any data missing.");
                }
            }

            return csvLines.stream()
                    .map(csvLine -> Agent.builder()
                            .type(csvLine.getType())
                            .name(csvLine.getName())
                            .agentId(csvLine.getAgentId())
                            .joiningDate(csvLine.getJoiningDate())
                            .cellNumber(csvLine.getCellNumber())
                            .bankAcNumber(csvLine.getBankAcNumber())
                            .nagadAcNumber(csvLine.getNagadAcNumber())
                            .bkashAcNumber(csvLine.getBkashAcNumber())
                            .status(true)
                            .password("FifoAgent007")
                            .build())
                    .collect(Collectors.toSet());
        }
    }

    private boolean isAnyFieldMissing(agentCsvRepresentation csvLine) {
        return isNullOrEmpty(csvLine.getType()) || isNullOrEmpty(csvLine.getName()) || isNullOrEmpty(csvLine.getAgentId()) ||
                isNullOrEmpty(csvLine.getJoiningDate()) || isNullOrEmpty(csvLine.getCellNumber()) || isNullOrEmpty(csvLine.getBankAcNumber()) ||
                isNullOrEmpty(csvLine.getNagadAcNumber()) || isNullOrEmpty(csvLine.getBkashAcNumber());
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public List<Agent> findAgentWithSorting(String field) {
        return repository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    public Page<Agent> findAgentsWithPagination(int offset, int pageSize) {
        return repository.findAll(PageRequest.of(offset, pageSize));
    }

    public Page<Agent> findAgentsWithPaginationAndSorting(int offset, int pageSize, String field) {
        return repository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));

    }

    public List<Agent> search(String name, String agentId, String type) {
        List<Agent> agents = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            agents.addAll(repository.findByNameContainingIgnoreCase(name));
        }
        if (agentId != null && !agentId.isEmpty()) {
            agents.addAll(repository.findByAgentId(agentId).map(Collections::singletonList).orElse(Collections.emptyList()));
        }
        if (type != null && !type.isEmpty()) {
            agents.addAll(repository.findByTypeContainingIgnoreCase(type));
        }

        // Remove duplicates if multiple criteria match the same agent
        return agents.stream().distinct().collect(Collectors.toList());
    }
}

