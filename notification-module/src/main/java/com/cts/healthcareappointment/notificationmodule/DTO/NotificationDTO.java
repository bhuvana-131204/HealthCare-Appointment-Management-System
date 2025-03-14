package com.cts.healthcareappointment.notificationmodule.DTO;

import com.cts.healthcareappointment.notificationmodule.Entity.NotificationStatus;
import com.cts.healthcareappointment.notificationmodule.Entity.NotificationType;

public class NotificationDTO {

    private int notificationId;
    private String id; // You can remove this field if not used
    private NotificationType type;
    private String message;
    private boolean seenStatus;
    private NotificationStatus status;
    private int doctorId;
    private String userId; // Maps the user ID directly

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeenStatus() {
        return seenStatus;
    }

    public void setSeenStatus(boolean seenStatus) {
        this.seenStatus = seenStatus;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

    