package com.mehediFifo.CRM.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private String qNo;
    @Column(length = 500)
    private String title;
    @Column(length = 500)
    private String type;
    @Column(length = 500)
    private String options;
    @Column(length = 500)
    private String instruction;
    @Column(length = 500)
    private String campaignId;

    // Initialize with default value
    private Boolean status = true;

    @Column(length = 500)
    private String optionalValue;

    @CreationTimestamp
    private LocalDateTime createAt;
}
