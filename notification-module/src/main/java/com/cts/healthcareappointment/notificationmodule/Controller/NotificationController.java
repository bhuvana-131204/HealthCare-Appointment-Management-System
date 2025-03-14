package com.cts.healthcareappointment.notificationmodule.Controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.healthcareappointment.notificationmodule.DTO.NotificationDTO;
import com.cts.healthcareappointment.notificationmodule.Entity.Notification;
import com.cts.healthcareappointment.notificationmodule.Exception.NotificationNotFoundException;
import com.cts.healthcareappointment.notificationmodule.service.NotificationService;

import jakarta.validation.Valid;

//import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/Pending")
    public ResponseEntity<List<Notification>> getAllPendingAppointments() {
        log.info("Fetching all pending appointments");
        List<Notification> notifications = notificationService.getAllPendingAppointments();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/Confirmed/{userId}")
    public ResponseEntity<List<Notification>> getAllConfirmedAppointments(@PathVariable String userId) {
        log.info("Fetching all confirmed appointments for user ID: {}", userId);
        List<Notification> notifications = notificationService.getAllConfirmedAppointments(userId);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/patient")
    public ResponseEntity<List<Notification>> getAllAppointmentStatusForPatients() {
        log.info("Fetching all appointment statuses for patients");
        List<Notification> notifications = notificationService.getAllAppointmentStatusForPatients();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(@RequestBody @Valid NotificationDTO notificationDTO, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Invalid notification data: {}", result.getFieldErrors());
            throw new IllegalArgumentException("Invalid notification data");
        }
        log.info("Received request to create notification: {}", notificationDTO);

        // Map NotificationDTO to Notification entity
        Notification notificationEntity = new Notification();
        notificationEntity.setType(notificationDTO.getType());
        notificationEntity.setStatus(notificationDTO.getStatus());
        notificationEntity.setMessage(notificationDTO.getMessage());
        notificationEntity.setDoctorId(notificationDTO.getDoctorId());
        notificationEntity.setSeenStatus(notificationDTO.isSeenStatus());
        notificationEntity.setUserId(notificationDTO.getUserId()); // Assuming this maps correctly

        // Call the service method
        Notification createdNotification = notificationService.createNotification(notificationEntity);
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    @PutMapping("/update/{notificationId}")
    public ResponseEntity<Notification> updateNotification(@PathVariable String notificationId, @RequestBody @Valid Notification notification, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Invalid notification data for update: {}", result.getFieldErrors());
            throw new IllegalArgumentException("Invalid notification data");
        }
        log.info("Updating notification with ID: {}", notificationId);
        Notification updatedNotification = notificationService.updateNotification(notificationId, notification);
        return new ResponseEntity<>(updatedNotification, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String notificationId) {
        log.info("Deleting notification with ID: {}", notificationId);
        notificationService.deleteNotification(notificationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        log.info("Fetching all notifications");
        List<Notification> notifications = notificationService.getAllNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
}