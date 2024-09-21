package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.service.AgentPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ap")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class AgentPanelController {
    @Autowired
    private AgentPanelService service;

//    @GetMapping("/getByNumber")
//    public Object getLeadByNumber(String tableName, String cellNumber) {
//        return service.getObjectByNumber(tableName, cellNumber);
//    }

    @GetMapping("/getByNumber")
    public ResponseEntity<?> getLeadByNumber(@RequestParam String tableName, @RequestParam String cellNumber) {
        Map<String, Object> result = service.getObjectByNumber(tableName, cellNumber);

        if (result.containsKey("message")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.ok(result);
        }
    }

}
