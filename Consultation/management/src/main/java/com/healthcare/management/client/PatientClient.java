package com.healthcare.management.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="USER-SERVICE")
public interface PatientClient {

}
