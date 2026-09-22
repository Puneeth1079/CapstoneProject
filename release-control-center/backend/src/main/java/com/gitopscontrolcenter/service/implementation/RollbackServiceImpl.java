package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.dto.RollbackResponse;
import com.gitopscontrolcenter.entity.RollbackEvent;
import com.gitopscontrolcenter.repository.RollbackEventRepository;
import com.gitopscontrolcenter.service.interfaces.RollbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RollbackServiceImpl implements RollbackService {

    private final RollbackEventRepository rollbackEventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RollbackResponse> getAllRollbacks() {
        return rollbackEventRepository.findTop10ByOrderByDetectedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RollbackResponse getRollbackById(Long id) {
        RollbackEvent event = rollbackEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rollback event not found with id: " + id));
        return mapToResponse(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RollbackResponse> getRollbacksByApplication(String appName) {
        return rollbackEventRepository.findByApplicationNameOrderByDetectedAtDesc(appName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RollbackResponse recordRollback(RollbackResponse response) {
        RollbackEvent event = RollbackEvent.builder()
                .applicationName(response.getApplicationName())
                .failedVersion(response.getFailedVersion())
                .restoredVersion(response.getRestoredVersion())
                .failureReason(response.getFailureReason())
                .failedMetric(response.getFailedMetric())
                .measuredValue(response.getMeasuredValue())
                .thresholdValue(response.getThresholdValue())
                .rolloutStage(response.getRolloutStage())
                .trafficExposure(response.getTrafficExposure())
                .recoveryDurationSeconds(response.getRecoveryDurationSeconds() != null ? response.getRecoveryDurationSeconds() : 45)
                .automatic(response.getAutomatic() != null ? response.getAutomatic() : true)
                .status(response.getStatus() != null ? response.getStatus() : "COMPLETED")
                .build();

        RollbackEvent saved = rollbackEventRepository.save(event);
        return mapToResponse(saved);
    }

    private RollbackResponse mapToResponse(RollbackEvent event) {
        return RollbackResponse.builder()
                .id(event.getId())
                .applicationName(event.getApplicationName())
                .failedVersion(event.getFailedVersion())
                .restoredVersion(event.getRestoredVersion())
                .failureReason(event.getFailureReason())
                .failedMetric(event.getFailedMetric())
                .measuredValue(event.getMeasuredValue())
                .thresholdValue(event.getThresholdValue())
                .rolloutStage(event.getRolloutStage())
                .trafficExposure(event.getTrafficExposure())
                .detectedAt(event.getDetectedAt())
                .rollbackStartedAt(event.getRollbackStartedAt())
                .recoveredAt(event.getRecoveredAt())
                .recoveryDurationSeconds(event.getRecoveryDurationSeconds())
                .automatic(event.getAutomatic())
                .status(event.getStatus())
                .build();
    }
}
