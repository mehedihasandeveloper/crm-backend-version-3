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
    private Integer totalDataTables;
    private Integer inboundTotal;   // New field for Inbound campaigns
    private Integer outboundTotal;  // New field for Outbound campaigns

    // Constructors
    public Statistics() {}

    public Statistics(Integer totalAgents, Integer totalCampaigns, Integer totalDataTables, Integer inboundTotal, Integer outboundTotal) {
        this.totalAgents = totalAgents;
        this.totalCampaigns = totalCampaigns;
        this.totalDataTables = totalDataTables;
        this.inboundTotal = inboundTotal;
        this.outboundTotal = outboundTotal;
    }

}
