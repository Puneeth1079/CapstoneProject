package com.cloudcart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaultModeRequest {

    @NotBlank(message = "Mode is required")
    private String mode; // NORMAL, HIGH_LATENCY, HTTP_ERRORS, FAILED_TRANSACTIONS, CPU_PRESSURE, MEMORY_PRESSURE

    private Double errorRate; // Optional probability override (0.0 to 1.0)
    private Long latencyMs;   // Optional latency delay override
}
