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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue
    private Long id;
    private String qNo;
    private String title;
    private String type;
    private String options;
    private String instruction;
    private String campaignId;

    // Initialize with default value
    private Boolean status = true;

    private String optionalValue;

    @CreationTimestamp
    private LocalDateTime createAt;
}
