package com.mehediFifo.CRM.service;

import com.mehediFifo.CRM.entity.Question;
import com.mehediFifo.CRM.repository.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public List<Question> addQuestions(List<Question> questions) {
        if (questions.isEmpty()) {
            return questions;
        }

        String campaignId = questions.get(0).getCampaignId();

        String checkTableQuery = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = :tableName";
        Query query = entityManager.createNativeQuery(checkTableQuery);
        query.setParameter("tableName", campaignId);
        Long tableCount = ((Number) query.getSingleResult()).longValue();

        int startQNo = 1;
        List<Question> existingQuestions = repository.findByCampaignId(campaignId);

        if (!existingQuestions.isEmpty()) {
            startQNo = existingQuestions.stream()
                    .mapToInt(question -> Integer.parseInt(question.getQNo().substring(1)))  // Remove "Q" and parse as integer
                    .max()
                    .orElse(1) + 1;
        }

        for (int i = 0; i < questions.size(); i++) {
            String qNo = "Q" + (startQNo + i);
            questions.get(i).setQNo(qNo);

            String checkColumnQuery = "SELECT COUNT(*) FROM information_schema.columns WHERE table_name = :tableName AND column_name = :columnName";
            Query columnQuery = entityManager.createNativeQuery(checkColumnQuery);
            columnQuery.setParameter("tableName", campaignId);
            columnQuery.setParameter("columnName", qNo);

            Long columnCount = ((Number) columnQuery.getSingleResult()).longValue();

            if (columnCount == 0) {
                String addColumnQuery = "ALTER TABLE " + campaignId + " ADD COLUMN " + qNo + " VARCHAR(255)";
                entityManager.createNativeQuery(addColumnQuery).executeUpdate();
            }
        }

        return repository.saveAll(questions);
    }



    public List<Question> findAllQuestions() {
        return repository.findAll();
    }

    public void deleteQuestion(Long id) {
        repository.deleteById(id);
    }

    public Question updateQuestion(Question question) {
        Question existingQuestion = repository.findById(question.getId()).orElse(question);
        existingQuestion.setTitle(question.getTitle());
        existingQuestion.setType(question.getType());
        existingQuestion.setOptions(question.getOptions());
        existingQuestion.setInstruction(question.getInstruction());
        existingQuestion.setOptionalValue(question.getOptionalValue());
        existingQuestion.setStatus(question.getStatus());
        return repository.save(existingQuestion);
    }

    public Question getQuestionById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Question> getToSetLogicQuestions(String campaignId) {
        return repository.findByCampaignId(campaignId);
    }

    public Page<Question> findQuestionsWithPagination(int offset, int pageSize) {
        return repository.findAll(PageRequest.of(offset, pageSize));
    }
    public List<Question> search(String campaignId, String title, String type, String options, String optionalValue, String instruction, Boolean status) {
        List<Question> questions = new ArrayList<>();
        if (campaignId != null && !campaignId.isEmpty()) {
            questions.addAll(repository.findByCampaignIdContainingIgnoreCase(campaignId));
        }
        if (title != null && !title.isEmpty()) {
            questions.addAll(repository.findByTitleContainingIgnoreCase(title));
        }
        if (type != null && !type.isEmpty()) {
            questions.addAll(repository.findByType(type)); // exact match
        }
        if (options != null && !options.isEmpty()) {
            questions.addAll(repository.findByOptionsContainingIgnoreCase(options));
        }
        if (optionalValue != null && !optionalValue.isEmpty()) {
            questions.addAll(repository.findByOptionalValueContainingIgnoreCase(optionalValue));
        }
        if (instruction != null && !instruction.isEmpty()) {
            questions.addAll(repository.findByInstruction(instruction)); // exact match
        }
        if (status != null) {
            questions.addAll(repository.findByStatus(status));
        }
        return questions.stream().distinct().collect(Collectors.toList());
    }
}
