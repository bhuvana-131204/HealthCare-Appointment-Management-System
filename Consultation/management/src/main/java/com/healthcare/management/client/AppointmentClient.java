package com.healthcare.management.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="APPOINTMENT-SERVICE")
public interface AppointmentClient {
	
	
	
}
