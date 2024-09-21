package com.mehediFifo.CRM.controller;


import com.mehediFifo.CRM.entity.Logic;
import com.mehediFifo.CRM.service.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logic")
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class LogicController {

    @Autowired
    private LogicService service;

    @PostMapping("/setLogics")
    public void addLogics(@RequestBody List<Logic> logics) {
        service.saveLogics(logics);
    }

    @GetMapping("/getAllCampaignLogic")
    public List<Logic> getAllCampaignLogic(String campaignName){
        return service.getAllCampaignLogic(campaignName);
    }

    @GetMapping("/getLogicByQuestionId")
    public List<Logic> getLogicByQuestionId(String questionId){
        return service.getLogicByQuestionId(questionId);
    }

    @GetMapping("/isLogicExist")
    public boolean isLogicExist(String id) {
        return service.isLogicExist(id);
    }

    @DeleteMapping("/clearLogic")
    public void clearLogic(String id) {
        service.deleteLogic(id);
    }

    @PutMapping("/updateLogic")
    public void updateLogic(@RequestBody List<Logic> logics) {
        service.updateLogic(logics);
    }

}
