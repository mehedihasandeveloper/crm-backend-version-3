package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.AgentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentLogRepository extends JpaRepository<AgentLog, Long> {
}
