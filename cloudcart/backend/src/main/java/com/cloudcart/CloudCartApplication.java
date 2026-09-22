package com.cloudcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CloudCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudCartApplication.class, args);
        System.out.println("CloudCartApplication started successfully.");
    }
}
