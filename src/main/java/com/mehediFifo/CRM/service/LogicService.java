package com.mehediFifo.CRM.service;

import com.mehediFifo.CRM.entity.Logic;
import com.mehediFifo.CRM.repository.LogicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogicService {
    @Autowired
    private LogicRepository repository;


    public void saveLogics(List<Logic> logics) {
        repository.saveAll(logics);
    }

    public boolean isLogicExist(String id) {
        return repository.existsByQuestionId(id);
    }
    @Transactional
    public void deleteLogic(String id) {
        repository.deleteByQuestionId(id);

    }

    public void updateLogic(List<Logic> logics) {

        for (Logic logic : logics)
        {
            Logic existinglogic = repository.findByOptionTitle(logic.getOptionTitle()).orElse(logic);
            existinglogic.setOptionTitle(logic.getOptionTitle());
            existinglogic.setQuestionId(logic.getQuestionId());
            existinglogic.setQuestionNoToShow(logic.getQuestionNoToShow());
            existinglogic.setIsCompletedCall(logic.getIsCompletedCall());
            existinglogic.setCampaignId(logic.getCampaignId());
            repository.save(existinglogic);
        }

    }

    public List<Logic> getAllCampaignLogic(String campaignName) {
        return repository.findAllByCampaignId(campaignName);
    }

    public List<Logic> getLogicByQuestionId(String questionId) {
        return repository.findAllByQuestionId(questionId);
    }
}
