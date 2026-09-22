package com.gitopscontrolcenter.integration.registry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RegistryClient {

    public List<Map<String, Object>> getImageTags(String imageName) {
        return List.of(
                Map.of("tag", "a82fc31", "digest", "sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "pushedAt", "2026-09-15T09:05:00Z", "sizeBytes", 184000000),
                Map.of("tag", "v1.2.0", "digest", "sha256:d41d8cd98f00b204e9800998ecf8427e", "pushedAt", "2026-09-14T14:35:00Z", "sizeBytes", 182000000),
                Map.of("tag", "v1.1.0", "digest", "sha256:8f4e5658e0a13d80a312e690069b22b1", "pushedAt", "2026-09-10T11:20:00Z", "sizeBytes", 180000000)
        );
    }
}
