package com.gitopscontrolcenter.service.implementation;

import com.gitopscontrolcenter.integration.kubernetes.KubernetesClient;
import com.gitopscontrolcenter.service.interfaces.KubernetesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KubernetesServiceImpl implements KubernetesService {

    private final KubernetesClient kubernetesClient;

    @Override
    public Map<String, Object> getClusterStatus() {
        return kubernetesClient.getClusterHealth();
    }
}
