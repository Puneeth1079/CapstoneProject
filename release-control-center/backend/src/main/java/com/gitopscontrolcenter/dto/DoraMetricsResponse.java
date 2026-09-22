package com.gitopscontrolcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoraMetricsResponse {

    private String period; // daily, weekly, monthly
    
    // 1. Deployment Frequency
    private Double deploymentFrequency; // per day
    private String deploymentFrequencyRating; // Elite, High, Medium, Low
    
    // 2. Lead Time for Changes
    private Double leadTimeHours;
    private String leadTimeRating;
    
    // 3. Change Failure Rate
    private Double changeFailureRatePct;
    private String changeFailureRateRating;
    
    // 4. Mean Time to Recovery
    private Double mttrMinutes;
    private String mttrRating;

    private Map<String, Object> historicalTrend;
}
