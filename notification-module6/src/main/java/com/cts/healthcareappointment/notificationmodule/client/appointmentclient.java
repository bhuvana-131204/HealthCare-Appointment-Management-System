package com.cts.healthcareappointment.notificationmodule.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="APPOINTMENT-SERVICE")
public interface appointmentclient {

}