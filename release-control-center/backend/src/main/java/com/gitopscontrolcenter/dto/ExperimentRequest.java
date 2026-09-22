package com.gitopscontrolcenter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentRequest {

    @NotBlank(message = "Experiment mode is required")
    private String mode; // NORMAL, HIGH_LATENCY, HTTP_ERRORS, FAILED_TRANSACTIONS, CPU_PRESSURE, MEMORY_PRESSURE

    private String description;
    private Double errorRate;
    private Long latencyMs;
}
