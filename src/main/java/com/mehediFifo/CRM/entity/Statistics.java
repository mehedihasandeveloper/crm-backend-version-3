package com.mehediFifo.CRM.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalAgents;
    private Integer totalCampaigns;
    // Constructors
    public Statistics() {}

    public Statistics(Integer totalAgents, Integer totalCampaigns) {
        this.totalAgents = totalAgents;
        this.totalCampaigns = totalCampaigns;
    }

}
