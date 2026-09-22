package com.gitopscontrolcenter.service.interfaces;

import com.gitopscontrolcenter.dto.RollbackResponse;

import java.util.List;

public interface RollbackService {
    List<RollbackResponse> getAllRollbacks();
    RollbackResponse getRollbackById(Long id);
    List<RollbackResponse> getRollbacksByApplication(String appName);
    RollbackResponse recordRollback(RollbackResponse rollbackResponse);
}
