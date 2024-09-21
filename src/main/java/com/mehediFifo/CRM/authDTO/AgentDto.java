package com.mehediFifo.CRM.authDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentDto {
    private String type;
    private String agentId;
    private String name;
    private String bankAcNumber;
    private String nagadAcNumber;
    private String bkashAcNumber;
    private String password;
    private String joiningDate;
    private String cellNumber;

    private Set<String> roles = new HashSet<>();


}
