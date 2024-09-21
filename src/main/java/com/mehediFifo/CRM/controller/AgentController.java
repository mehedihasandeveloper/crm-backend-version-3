package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.config.APIResponse;
import com.mehediFifo.CRM.customException.InvalidDataException;
import com.mehediFifo.CRM.entity.Agent;
import com.mehediFifo.CRM.entity.Statistics;
import com.mehediFifo.CRM.repository.StatisticsRepository;
import com.mehediFifo.CRM.service.AgentService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/ac")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class AgentController {

    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);
    @Autowired
    private AgentService service;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @PostMapping("/addAgent")
    public Agent addAgent(@RequestBody Agent agent) {
        return service.addAgent(agent);
    }

    @GetMapping("/getAgentList")
    public List<Agent> getAgentList() {
        return service.getAgentList();
    }

    @DeleteMapping("/deleteAgent/{id}")
    public void deleteAgent(@PathVariable Long id) {
        service.removeAgent(id);
    }

    @PutMapping("/editAgent")
    public Agent updateAgent(@RequestBody Agent agent) {
        return service.editAgent(agent);
    }

    @GetMapping("/getAgent/{id}")
    public Agent getAgent(@PathVariable Long id) {
        return service.findAgent(id);
    }

    @PostMapping(value = "upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Integer> uploadAgents(@RequestPart("file") MultipartFile file) throws IOException, InvalidDataException {
        return ResponseEntity.ok(service.uploadAgents(file));
    }

    @GetMapping("/{field}")
    public APIResponse<List<Agent>> getAgentListWithSort(@PathVariable String field) {
        List<Agent> allAgents = service.findAgentWithSorting(field);
        return new APIResponse<>(allAgents.size(), allAgents);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public APIResponse<Page<Agent>> getAgentListWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Agent> agentsWithPagination = service.findAgentsWithPagination(offset, pageSize);
        return new APIResponse<>(agentsWithPagination.getSize(), agentsWithPagination);
    }

    @GetMapping("/paginationAndSort/{offset}/{pageSize}/{field}")
    public APIResponse<Page<Agent>> getAgentListWithPaginationAndSort(@PathVariable int offset, @PathVariable int pageSize, @PathVariable String field) {
        Page<Agent> agentsWithPagination = service.findAgentsWithPaginationAndSorting(offset, pageSize, field);
        return new APIResponse<>(agentsWithPagination.getSize(), agentsWithPagination);
    }


    @GetMapping("/search")
    public List<Agent> search(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "agentId", required = false) String agentId,
                              @RequestParam(value = "type", required = false) String type) {
        return service.search(name, agentId, type);
    }

    @GetMapping("/totalAgents")
    public Integer getTotalAgents() {
        Optional<Statistics> stats = statisticsRepository.findById(1L);
        return stats.map(Statistics::getTotalAgents).orElse(0);
    }

}
