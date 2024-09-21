package com.mehediFifo.CRM.service;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class agentCsvRepresentation {
    @CsvBindByName(column = "type")
    private String type;
    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "agentId")
    private String agentId;
    @CsvBindByName(column = "joiningDate")
    private String joiningDate;
    @CsvBindByName(column = "cellNumber")
    private String cellNumber;
    @CsvBindByName(column = "bankAcNumber")
    private String bankAcNumber;
    @CsvBindByName(column = "nagadAcNumber")
    private String nagadAcNumber;
    @CsvBindByName(column = "bkashAcNumber")
    private String bkashAcNumber;

}
