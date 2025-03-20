package com.cts.healthcareappointment.notificationmodule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cts.healthcareappointment.notificationmodule.Dao.NotificationDao;
import com.cts.healthcareappointment.notificationmodule.Entity.Notification;
import com.cts.healthcareappointment.notificationmodule.Entity.NotificationStatus;
import com.cts.healthcareappointment.notificationmodule.Entity.NotificationType;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private NotificationDao notificationDao;

   // @Autowired
    //private RestTemplate restTemplate;

 
    
    /**
     * Fetch all notifications by userId.
     */
    public List<Notification> getNotificationsByUserId(String userId) {
        log.info("Fetching notifications for userId: {}", userId);
        return notificationDao.findByUserId(userId);
    }
    
    /**
     * Notify users and patients when an appointment is booked.
     */
    public void notifyAfterBooking(Notification bookingNotification) {
        log.info("Processing booking notification: {}", bookingNotification);
        try {
            bookingNotification.setType(NotificationType.APPOINTMENT);
            bookingNotification.setStatus(NotificationStatus.Confirmed);
            bookingNotification.setSeenStatus(false);
            notificationDao.save(bookingNotification);

            log.info("Booking notification saved successfully.");
            System.out.println("Notification sent successfully to patient and doctor for booking.");
        } catch (Exception ex) {
            log.error("Error while sending booking notification: {}", ex.getMessage());
            throw new RuntimeException("Error in sending notification for appointment booking", ex);
        }
    }

    /**
     * Update an existing notification by its ID.
     */
    public Notification updateNotification(String notificationId, Notification notificationDetails) {
        log.info("Updating notification with ID: {}", notificationId);

        // Fetch the existing notification from the database
        Notification existingNotification = notificationDao.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));

        // Update the relevant fields
        existingNotification.setType(notificationDetails.getType());
        existingNotification.setStatus(notificationDetails.getStatus());
        existingNotification.setMessage(notificationDetails.getMessage());
        existingNotification.setDoctorId(notificationDetails.getDoctorId());
        existingNotification.setSeenStatus(notificationDetails.isSeenStatus());
        existingNotification.setUserId(notificationDetails.getUserId());

        // Save the updated notification
        Notification updatedNotification = notificationDao.save(existingNotification);
        log.info("Updated notification with ID: {}", notificationId);
        return updatedNotification;
    }

    
    
    /**
     * Notify users and patients when an appointment is canceled.
     */
    public void notifyAfterCancellation(Notification cancellationNotification) {
        log.info("Processing cancellation notification: {}", cancellationNotification);
        try {
            cancellationNotification.setType(NotificationType.APPOINTMENT);
            cancellationNotification.setStatus(NotificationStatus.Cancelled);
            cancellationNotification.setSeenStatus(false);
            notificationDao.save(cancellationNotification);

            log.info("Cancellation notification saved successfully.");
            System.out.println("Notification sent successfully to patient and doctor for cancellation.");
        } catch (Exception ex) {
            log.error("Error while sending cancellation notification: {}", ex.getMessage());
            throw new RuntimeException("Error in sending notification for appointment cancellation", ex);
        }
    }

    /**
     * Notify Appointment Module when an appointment is completed based on the time slot.
     */
    @Scheduled(fixedRate = 60000)
    public void notifyAppointmentCompletion() {
        log.info("Checking for appointments to mark as completed...");
        try {
            List<Notification> notifications = notificationDao.findAll();
            LocalDateTime now = LocalDateTime.now();

            for (Notification notification : notifications) {
                if (notification.getStatus() == NotificationStatus.Confirmed && notification.getType() == NotificationType.APPOINTMENT) {
                    log.info("Marking appointment as completed: {}", notification.getNotification_id());

                    notification.setStatus(NotificationStatus.Completed);
                    notification.setSeenStatus(true);
                    notificationDao.save(notification);

                    log.info("Appointment completion notification processed successfully.");
                    System.out.println("Notification sent successfully to Appointment Module for completion.");
                }
            }
        } catch (Exception ex) {
            log.error("Error while notifying appointment completion: {}", ex.getMessage());
        }
    }

    /**
     * Create a new notification.
     */
    public Notification createNotification(Notification notification) {
        log.info("Creating a new notification: {}", notification);
        try {
            return notificationDao.save(notification);
        } catch (Exception ex) {
            log.error("Error while creating notification: {}", ex.getMessage());
            throw new RuntimeException("Unable to create notification", ex);
        }
    }


    /**
     * Fetch all notifications for logging or monitoring purposes.
     */
    public List<Notification> getAllNotifications() {
        log.info("Fetching all notifications...");
        return notificationDao.findAll();
    }
}
