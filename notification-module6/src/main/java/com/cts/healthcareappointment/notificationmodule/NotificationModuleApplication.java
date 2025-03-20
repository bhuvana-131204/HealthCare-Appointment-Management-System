package com.cts.healthcareappointment.notificationmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotificationModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationModuleApplication.class, args);
    }
}
