package com.availabilitySchedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.availabilitySchedule.repository")
public class AvailabilityScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvailabilityScheduleApplication.class, args);
	}

}
