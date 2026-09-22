package com.cloudcart.service.implementation;

import com.cloudcart.dto.FaultModeRequest;
import com.cloudcart.dto.FaultModeResponse;
import com.cloudcart.service.interfaces.FaultSimulationService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class FaultSimulationServiceImpl implements FaultSimulationService {

    @Value("${cloudcart.fault.default-mode:NORMAL}")
    private String defaultMode;

    private String currentMode = "NORMAL";
    private double errorRate = 0.50; // 50% failure rate when HTTP_ERRORS is active
    private long latencyMs = 2500;   // 2.5 second delay when HIGH_LATENCY is active
    private ZonedDateTime lastUpdated = ZonedDateTime.now();
    private final Random random = new Random();
    private final AtomicInteger activeFaultGauge = new AtomicInteger(0);

    private final MeterRegistry meterRegistry;
    private Counter faultTriggerCounter;

    public FaultSimulationServiceImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        this.currentMode = defaultMode.toUpperCase();
        Gauge.builder("cloudcart.fault.active", activeFaultGauge, AtomicInteger::get)
                .description("Active fault mode state indicator (0=NORMAL, 1=HIGH_LATENCY, 2=HTTP_ERRORS, 3=FAILED_TRANSACTIONS, 4=CPU_PRESSURE, 5=MEMORY_PRESSURE)")
                .register(meterRegistry);

        this.faultTriggerCounter = Counter.builder("cloudcart.fault.triggers")
                .description("Total number of simulated fault triggers")
                .register(meterRegistry);

        updateGauge();
    }

    @Override
    public synchronized FaultModeResponse setFaultMode(FaultModeRequest request) {
        String mode = request.getMode() != null ? request.getMode().toUpperCase() : "NORMAL";
        this.currentMode = mode;
        if (request.getErrorRate() != null && request.getErrorRate() >= 0 && request.getErrorRate() <= 1.0) {
            this.errorRate = request.getErrorRate();
        }
        if (request.getLatencyMs() != null && request.getLatencyMs() >= 0) {
            this.latencyMs = request.getLatencyMs();
        }
        this.lastUpdated = ZonedDateTime.now();
        updateGauge();
        log.info("Fault mode changed to: {} (errorRate={}, latencyMs={})", currentMode, errorRate, latencyMs);
        return getFaultMode();
    }

    @Override
    public FaultModeResponse getFaultMode() {
        String desc;
        switch (currentMode) {
            case "HIGH_LATENCY" -> desc = "Injecting bounded artificial delay (" + latencyMs + "ms)";
            case "HTTP_ERRORS" -> desc = "Returning HTTP 500 Internal Server Errors on " + (int)(errorRate * 100) + "% of requests";
            case "FAILED_TRANSACTIONS" -> desc = "Simulating order placement transaction failures";
            case "CPU_PRESSURE" -> desc = "Executing safe CPU computation loops";
            case "MEMORY_PRESSURE" -> desc = "Allocating bounded temporary memory buffers";
            default -> desc = "System operating under normal condition";
        }

        return FaultModeResponse.builder()
                .activeMode(currentMode)
                .errorRate(errorRate)
                .latencyMs(latencyMs)
                .active(!"NORMAL".equals(currentMode))
                .description(desc)
                .updatedTime(lastUpdated)
                .build();
    }

    @Override
    public FaultModeResponse resetFaultMode() {
        return setFaultMode(FaultModeRequest.builder().mode("NORMAL").build());
    }

    @Override
    public void evaluateAndApplyFault() {
        if ("NORMAL".equals(currentMode)) {
            return;
        }

        faultTriggerCounter.increment();

        if ("HIGH_LATENCY".equals(currentMode)) {
            try {
                log.warn("Fault Injection [HIGH_LATENCY]: Sleeping for {}ms", latencyMs);
                Thread.sleep(latencyMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else if ("HTTP_ERRORS".equals(currentMode)) {
            if (random.nextDouble() < errorRate) {
                log.warn("Fault Injection [HTTP_ERRORS]: Throwing simulated HTTP 500 exception");
                throw new RuntimeException("Simulated Fault Mode: Controlled HTTP 500 Internal Server Error");
            }
        } else if ("CPU_PRESSURE".equals(currentMode)) {
            long startTime = System.currentTimeMillis();
            log.warn("Fault Injection [CPU_PRESSURE]: Simulating CPU load for 300ms");
            while (System.currentTimeMillis() - startTime < 300) {
                Math.sqrt(random.nextDouble() * random.nextDouble());
            }
        }
    }

    @Override
    public boolean shouldFailTransaction() {
        if ("FAILED_TRANSACTIONS".equals(currentMode)) {
            faultTriggerCounter.increment();
            return random.nextDouble() < errorRate;
        }
        return false;
    }

    private void updateGauge() {
        int code = switch (currentMode) {
            case "HIGH_LATENCY" -> 1;
            case "HTTP_ERRORS" -> 2;
            case "FAILED_TRANSACTIONS" -> 3;
            case "CPU_PRESSURE" -> 4;
            case "MEMORY_PRESSURE" -> 5;
            default -> 0;
        };
        activeFaultGauge.set(code);
    }
}
