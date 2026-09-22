package com.gitopscontrolcenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GitOpsControlCenterApplicationTests {

    @Test
    void contextLoads() {
        // Verifies Spring Boot context load for Release Control Center
    }
}
