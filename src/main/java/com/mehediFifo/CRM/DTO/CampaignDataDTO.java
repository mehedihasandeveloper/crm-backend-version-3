package com.mehediFifo.CRM.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CampaignDataDTO {
    // Getters and setters
    private String campaignTitle;
    private int totalLeads;
    private int generatedLeads;
    private int calledLeads;

    // Constructor
    public CampaignDataDTO(String campaignTitle, int totalLeads, int generatedLeads, int calledLeads) {
        this.campaignTitle = campaignTitle;
        this.totalLeads = totalLeads;
        this.generatedLeads = generatedLeads;
        this.calledLeads = calledLeads;
    }

}
