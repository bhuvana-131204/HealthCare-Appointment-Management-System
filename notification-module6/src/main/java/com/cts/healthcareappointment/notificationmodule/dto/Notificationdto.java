package com.cts.healthcareappointment.notificationmodule.dto;

import com.cts.healthcareappointment.notificationmodule.Entity.NotificationStatus;
import com.cts.healthcareappointment.notificationmodule.Entity.NotificationType;
import lombok.Data;

@Data
public class Notificationdto {

    private int notificationId;
    private String id; // You can remove this field if not used
    private NotificationType type;
    private String message;
    private boolean seenStatus;
    private NotificationStatus status;
    private int doctorId;
    private String userId; // Maps the user ID directly
}