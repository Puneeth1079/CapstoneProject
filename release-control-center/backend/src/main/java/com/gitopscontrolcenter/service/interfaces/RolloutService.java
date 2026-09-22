package com.gitopscontrolcenter.service.interfaces;

import com.gitopscontrolcenter.dto.RolloutResponse;

import java.util.List;

public interface RolloutService {
    List<RolloutResponse> getAllRollouts();
    RolloutResponse getRolloutByApplication(String applicationName);
    RolloutResponse promoteRollout(String applicationName);
    RolloutResponse pauseRollout(String applicationName);
    RolloutResponse resumeRollout(String applicationName);
    RolloutResponse abortRollout(String applicationName);
    RolloutResponse retryRollout(String applicationName);
}
