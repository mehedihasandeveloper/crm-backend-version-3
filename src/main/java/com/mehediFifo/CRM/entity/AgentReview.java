package com.mehediFifo.CRM.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AgentReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String callDate;
    private String consumerNumber;
    private Integer total;
    private String suggestion;
    private String qcInspector;
    private String agreed;
    private Boolean raiseConcern;
    @Column(length = 500)
    private String comment;
    private Boolean isResolved = false;
    @Column(length = 500)
    private String resolvedMessage;
    private Long qcId;
}
