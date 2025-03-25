package com.healthcare.management.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(name="NOTIFICATION-SERVICE")
public interface NotificationClient {
	
	
	@PutMapping("/api/notification/onCompletion/{appointmentId}")
	public void onCompletetion (@PathVariable String appointmentId);

}
