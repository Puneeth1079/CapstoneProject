package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.ExperimentRequest;
import com.gitopscontrolcenter.dto.ExperimentResponse;
import com.gitopscontrolcenter.entity.ReleaseExperiment;
import com.gitopscontrolcenter.repository.ReleaseExperimentRepository;
import com.gitopscontrolcenter.service.interfaces.ActivityService;
import com.gitopscontrolcenter.service.interfaces.ReleaseExperimentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.ZonedDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleaseExperimentServiceImpl implements ReleaseExperimentService {

    private final ReleaseExperimentRepository experimentRepository;
    private final ActivityService activityService;
    private final WebClient webClient;

    @Value("${devops.cloudcart.url:http://localhost:8081}")
    private String cloudcartUrl;

    @Override
    @Transactional
    public ExperimentResponse triggerExperiment(ExperimentRequest request) {
        String mode = request.getMode() != null ? request.getMode().toUpperCase() : "NORMAL";

        // Deactivate previous experiments
        experimentRepository.findAll().forEach(exp -> {
            if (exp.getActive()) {
                exp.setActive(false);
                exp.setEndedAt(ZonedDateTime.now());
                exp.setStatus("COMPLETED");
                experimentRepository.save(exp);
            }
        });

        ReleaseExperiment experiment = ReleaseExperiment.builder()
                .mode(mode)
                .description(request.getDescription() != null ? request.getDescription() : "Controlled failure injection: " + mode)
                .active(!"NORMAL".equals(mode))
                .startedAt(ZonedDateTime.now())
                .status(!"NORMAL".equals(mode) ? "RUNNING" : "COMPLETED")
                .build();

        ReleaseExperiment saved = experimentRepository.save(experiment);

        // Notify CloudCart backend fault API
        try {
            webClient.post()
                    .uri(cloudcartUrl + "/api/demo/fault-mode")
                    .bodyValue(Map.of("mode", mode))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .subscribe(
                            res -> log.info("Successfully triggered fault mode {} on CloudCart", mode),
                            err -> log.warn("Could not reach CloudCart at {} ({})", cloudcartUrl, err.getMessage())
                    );
        } catch (Exception e) {
            log.warn("Failed to dispatch fault mode to CloudCart: {}", e.getMessage());
        }

        activityService.recordActivity("EXPERIMENT_TRIGGERED",
                "Release Experiment Started: " + mode,
                experiment.getDescription(),
                "cloudcart-backend",
                !"NORMAL".equals(mode) ? "WARNING" : "INFO");

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ExperimentResponse getActiveExperiment() {
        return experimentRepository.findTopByActiveTrueOrderByStartedAtDesc()
                .map(this::mapToResponse)
                .orElseGet(() -> ExperimentResponse.builder()
                        .mode("NORMAL")
                        .description("System running normally under baseline conditions")
                        .active(false)
                        .startedAt(ZonedDateTime.now())
                        .status("COMPLETED")
                        .build());
    }

    @Override
    @Transactional
    public ExperimentResponse resetExperiment() {
        return triggerExperiment(ExperimentRequest.builder().mode("NORMAL").description("Reset to normal operation").build());
    }

    private ExperimentResponse mapToResponse(ReleaseExperiment exp) {
        return ExperimentResponse.builder()
                .id(exp.getId())
                .mode(exp.getMode())
                .description(exp.getDescription())
                .active(exp.getActive())
                .startedAt(exp.getStartedAt())
                .endedAt(exp.getEndedAt())
                .status(exp.getStatus())
                .build();
    }
}
