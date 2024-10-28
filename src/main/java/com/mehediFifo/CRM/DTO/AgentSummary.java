package com.mehediFifo.CRM.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter

@Getter
@NoArgsConstructor
public class AgentSummary {
    // Getters and Setters
    private String agentName;
    private String agentId;
    private String red;
    private String yellow;
    private String blue;
    private String green;
    private String suggestions;

    public AgentSummary(String agentName, String agentId, String red, String yellow, String blue, String green, String suggestions) {
        this.agentName = agentName;
        this.agentId = agentId;
        this.red = red;
        this.yellow = yellow;
        this.blue = blue;
        this.green = green;
        this.suggestions = suggestions;
    }

}

