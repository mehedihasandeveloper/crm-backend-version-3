package com.mehediFifo.CRM.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgentLog {
    @Id
    @GeneratedValue
    private Long id;
    private String agentId;
    private String employeeType;
    private String loginShift;
    private String isLoggedIn;
    @CreationTimestamp
    private LocalDateTime login;
    private String extra_paused;
    private String call_taken;
    private String adjusted;
    private String remarks;
    private String campaignId;
    private String isLoggedOut;
    @CreationTimestamp
    private LocalDateTime logout;
    private String actualWorktime;
    private String workTime;
    private String pauseTime;
    private String day;
    private String month;
    private String year;
    private String payStatus;
    @CreationTimestamp
    private LocalDateTime createdAt;


}
