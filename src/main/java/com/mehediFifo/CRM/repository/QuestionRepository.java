package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCampaignId(String campaignId);

    List<Question> findByStatus(Boolean status);

    List<Question> findByCampaignIdContainingIgnoreCase(String campaignId);

    List<Question> findByTitleContainingIgnoreCase(String title);

    List<Question> findByType(String type);

    List<Question> findByOptionsContainingIgnoreCase(String options);

    List<Question> findByOptionalValueContainingIgnoreCase(String optionalValue);

    List<Question> findByInstruction(String instruction);
}
