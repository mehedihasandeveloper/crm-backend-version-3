package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findByName(String name);

    List<Agent> findByType(String type);

    Optional<Agent> findByAgentId(String agentId);


    boolean existsByAgentId(String agentId);


    List<Agent> findByNameContainingIgnoreCase(String name);

    List<Agent> findByTypeContainingIgnoreCase(String type);


}
