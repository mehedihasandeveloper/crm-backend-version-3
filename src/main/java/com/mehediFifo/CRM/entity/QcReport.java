package com.mehediFifo.CRM.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QcReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(length = 500)
    private String callDate;
    private String agentName;
    private String agentId;
    private String campaignName;
    private String consumerNumber;
    private String duration;
    private int greetings;
    private int liveliness;
    private int pronunciation;
    private int mumbling;
    private int pace;
    private int pitch;
    private int courtesy;
    private int holdProcess;
    private int takingPermission;
    private int acknowledgementAndFollowUp;
    private int poorObjectionAndNegotiationSkill;
    private int crm;
    private int fatal;
    private String fatalReason;
    private String easVoiceMatchedWithReport;
    private Integer total;
    private String agentGrade;
    @Column(length = 500)
    private String suggestion;
    private String qcInspector;
}
