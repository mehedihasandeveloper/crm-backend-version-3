package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.Logic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogicRepository extends JpaRepository<Logic, Long> {
    boolean existsByQuestionId(String id);

    void deleteByQuestionId(String id);


    List<Logic> findAllByCampaignId(String campaignName);

    List<Logic> findAllByQuestionId(String questionId);

    Optional<Logic> findByOptionTitle(String optionTitle);
}
