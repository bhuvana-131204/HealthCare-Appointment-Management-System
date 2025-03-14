package com.cts.healthcareappointment.notificationmodule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cts.healthcareappointment.notificationmodule.Dao.NotificationDao;
import com.cts.healthcareappointment.notificationmodule.Entity.Notification;
import com.cts.healthcareappointment.notificationmodule.Entity.NotificationStatus;
import com.cts.healthcareappointment.notificationmodule.Entity.NotificationType;
import com.cts.healthcareappointment.notificationmodule.Entity.User;
import com.cts.healthcareappointment.notificationmodule.Exception.NotificationNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    public List<Integer> getAllUserIds() {
        log.info("Fetching all user IDs");
        return List.of(1, 2); // Replace with actual user IDs later
    }

    public List<Notification> getAllPendingAppointments() {
        log.info("Fetching all pending appointments");
        List<Notification> notifications = notificationDao.findPendingAppointmentsForDoctor(NotificationType.APPOINTMENT, NotificationStatus.Pending);
        if (notifications.isEmpty()) {
            log.warn("No pending appointments found");
            throw new NotificationNotFoundException("No pending notifications found");
        }
        return notifications;
    }
    
  
  /*  public List<Notification> getAllPendingAppointments(int doctorId) {
        log.info("Fetching all pending appointments for doctor ID: {}", doctorId);
        List<Notification> notifications = notificationDao.findPendingAppointmentsForDoctor(NotificationType.APPOINTMENT, NotificationStatus.Pending, doctorId);
        if (notifications.isEmpty()) {
            log.warn("No pending appointments found for doctor ID: {}", doctorId);
            throw new NotificationNotFoundException("No pending notifications found");
        }
        return notifications;
    }
    */

    public List<Notification> getAllConfirmedAppointments(String userId) {
        log.info("Fetching all confirmed appointments");
        List<Notification> notifications = notificationDao.findConfirmedAppointmentsForDoctor(NotificationType.APPOINTMENT,userId, NotificationStatus.Confirmed);
        if (notifications.isEmpty()) {
            log.warn("No confirmed appointments found");
            throw new NotificationNotFoundException("No confirmed notifications found");
        }
        return notifications;
    }
    /*
   public List<Notification> getAllConfirmedAppointments(String userId) {
        log.info("Fetching all confirmed appointments for user ID: {}", userId);
        List<Notification> notifications = notificationDao.findConfirmedAppointmentsForDoctor(NotificationType.APPOINTMENT, NotificationStatus.Confirmed, userId);
        if (notifications.isEmpty()) {
            log.warn("No confirmed appointments found for user ID: {}", userId);
            throw new NotificationNotFoundException("No confirmed notifications found");
        }
        return notifications;
    }
*/
    public List<Notification> getAllAppointmentStatusForPatients() {
        log.info("Fetching all appointment status for patients");
        List<Notification> notifications = notificationDao.findAppointmentStatusForPatient(NotificationType.APPOINTMENT, List.of(NotificationStatus.Confirmed, NotificationStatus.Cancelled, NotificationStatus.Pending));
        if (notifications.isEmpty()) {
            log.warn("No appointment status notifications found");
            throw new NotificationNotFoundException("No appointment status notifications found");
        }
        return notifications;
    }

    public Notification createNotification(Notification notification) {
        /*log.info("Creating a new notification: {}", notification);*/

        // Fetch the User object based on userId
       /* User user = entityManager.find(User.class, notification.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        // Set the User object in the Notification entity
        notification.setNotify(user);

        // Save the Notification entity
        return notificationDao.save(notification);
    }*/
        log.info("Creating a new notification: {}", notification);
        return notificationDao.save(notification);
    }

    public Notification updateNotification(String notificationId, Notification notification) {
        log.info("Updating notification with ID: {}", notificationId);
        Notification existingNotification = notificationDao.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));

        existingNotification.setType(notification.getType());
        existingNotification.setStatus(notification.getStatus());
        existingNotification.setMessage(notification.getMessage());

        return notificationDao.save(existingNotification);
    }

    public void deleteNotification(String notificationId) {
        log.info("Deleting notification with ID: {}", notificationId);
        notificationDao.deleteById(notificationId);
    }

    public List<Notification> getAllNotifications() {
        log.info("Fetching all notifications");
        return notificationDao.findAll();
    }

    public void sendNotificationToDoctor(int userId, String message) {
        log.info("Notification to Doctor (User ID: {}): {}", userId, message);
    }

    public void sendNotificationToPatient(int userId, String message) {
        log.info("Notification to Patient (User ID: {}): {}", userId, message);
    }

    @Scheduled(fixedRate = 3600000)
    public void checkPendingNotifications() {
        log.info("Checking pending notifications");
        List<Integer> userIds = getAllUserIds();

        for (int userId : userIds) {
            try {
                List<Notification> pendingNotifications = getAllPendingAppointments();
                for (Notification notification : pendingNotifications) {
                    String message = "Upcoming appointment with notification ID: " + notification.getNotification_id();
                    log.info("Sending pending notification to doctor. User ID: {}, Message: {}", userId, message);
                    sendNotificationToDoctor(userId, message);
                }
            } catch (NotificationNotFoundException e) {
                log.warn("No pending notifications found for user ID: {}", userId, e);
            } /*catch (Exception e) {
                log.error("Error processing notifications for user ID: {}", userId, e);
            }*/
        }
    }

    @Scheduled(fixedRate = 3600000)
    public void sendAppointmentStatusNotifications() {
        log.info("Sending appointment status notifications");
        List<Integer> userIds = getAllUserIds();

        for (int userId : userIds) {
            try {
                List<Notification> notifications = getAllAppointmentStatusForPatients();
                for (Notification notification : notifications) {
                    String message;
                    switch (notification.getStatus()) {
                        case Confirmed:
                            message = "Your appointment is confirmed with doctor ID: " + notification.getDoctorId();
                            break;
                        case Cancelled:
                            message = "Your appointment is cancelled with doctor ID: " + notification.getDoctorId();
                            break;
                        case Pending:
                            message = "Your appointment is in pending status with doctor ID: " + notification.getDoctorId();
                            break;
                        default:
                            throw new IllegalArgumentException("Unexpected status: " + notification.getStatus());
                    }
                    log.info("Sending notification to patient. User ID: {}, Message: {}", userId, message);
                    sendNotificationToPatient(userId, message);
                }
            } catch (NotificationNotFoundException e) {
                log.warn("No appointment status notifications found for user ID: {}", userId, e);
            } catch (Exception e) {
                log.error("Error sending appointment status notifications for user ID: {}", userId, e);
            }
        }
    }
}

