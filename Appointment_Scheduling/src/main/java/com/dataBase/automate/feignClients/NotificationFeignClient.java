 package com.dataBase.automate.feignClients;
 
 import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dataBase.automate.dto.NotificationDto;
import com.dataBase.automate.dto.Response;
 
 @FeignClient("NOTIFICATION-SERVICE")
public interface NotificationFeignClient {
	 
	 @PostMapping("/checkCompletion")
	    public ResponseEntity<Response<?>> checkAppointmentsCompletion() ;
	 /**
	     * Notify the user and patient after an appointment is booked.
	     */
	    @PostMapping("/afterBooking")
	    public ResponseEntity<Response<?>> notifyAfterBooking(@RequestBody NotificationDto notification);
	 
	    /**
	     * Notify the user and patient after an appointment is canceled.
	     */
	    @PostMapping("/afterCancellation")
	    public ResponseEntity<Response<?>> notifyAfterCancellation(@RequestBody NotificationDto notification);
	    
	    
	    @PutMapping("/update/{notificationId}")
	    public ResponseEntity<Response<?>> updateNotification(@PathVariable String notificationId, @RequestBody NotificationDto notification);
}
