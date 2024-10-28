package com.mehediFifo.CRM.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
public class DateRangeSummary {
    private String agentName;
    private String agentId;
    private Map<String, Double> dailyAverages; // date -> average for that date
    private double sum;
    private long count;
    private double average;
}
