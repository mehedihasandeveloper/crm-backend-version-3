package com.mehediFifo.CRM.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QcReportClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String callDate;
    private String agentName;
    private String agentId;
    private String campaignName;
    private String consumerNumber;
    private String duration;
    private int greetingsMarks;
    private int livelinessMarks;
    private int pronunciationMarks;
    private int mumblingMarks;
    private int paceMarks;
    private int pitchMarks;
    private int courtesyMarks;
    private int holdProcessMarks;
    private int takingPermissionMarks;
    private int acknowledgementAndFollowUpMarks;
    private int poorObjectionAndNegotiationSkillMarks;
    private int crmMarks;
    private int closingMarks;
    private int fatalMarks;
    private String fatalReasonMarks;
    private String easVoiceMatchedWithReportMarks;
    private Integer total;
    private String qcInspector;
    private String agentGrade;
    @Column(length = 500)
    private String suggestionMarks;
}
