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
public class Logic {
    @Id
    @GeneratedValue
    private Long id;
    private String questionId;
    private String optionTitle;
    private String questionNoToShow;
    private String isCompletedCall;
    private String campaignId;
    @CreationTimestamp
    private LocalDateTime createAt;

}
