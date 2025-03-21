 package com.dataBase.automate.feignClients;
 
 import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dataBase.automate.dto.AppointmentDto;
import com.dataBase.automate.dto.NotificationDto;
import com.dataBase.automate.dto.Response;
/**
* Notification Service Feign Client.
* 
* @Author Sanjay R
* @Since 2025-03-18
*/
 @FeignClient("NOTIFICATION-SERVICE")
public interface NotificationFeignClient {
	 
	 
	 
	 @PostMapping("/notifications/create")
	  public ResponseEntity<Response<AppointmentDto>> createNotification(@RequestBody AppointmentDto appointmentDto);
	 
	 

	 @PutMapping("/notifications/onUpdate/{appointmentId}")
	 public void onUpdate(@PathVariable String appointmentId);
	 
}