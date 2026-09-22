package com.gitopscontrolcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GitOpsControlCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitOpsControlCenterApplication.class, args);
    }
}
