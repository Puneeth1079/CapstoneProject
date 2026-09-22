package com.gitopscontrolcenter.service.interfaces;

import java.util.Map;

public interface KubernetesService {
    Map<String, Object> getClusterStatus();
}
