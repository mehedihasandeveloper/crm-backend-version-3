package com.mehediFifo.CRM.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String agreed; // New field for Agreed status
    private Boolean raiseConcern; // New field for Raise Concern
    private Long qcId;
}
