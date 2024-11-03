package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.AgentReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentReviewRepository extends JpaRepository<AgentReview, Long> {
    List<AgentReview> findByQcId(Long qcId);
}
