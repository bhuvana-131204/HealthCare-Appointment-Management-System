package com.cts.healthcareappointment.notificationmodule.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @Column(name = "Notification_id", nullable = false, unique = true)
    private String notification_id;

    @NotNull(message = "Notification type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private NotificationType type;

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 255, message = "Message must be less than 255 characters")
    @Column(name = "Message", nullable = false)
    private String message;

    @Column(name = "SeenStatus", nullable = false)
    private boolean seenStatus = false;

    @NotNull(message = "Notification status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private NotificationStatus status;

    @Min(value = 1, message = "Doctor ID must be greater than 0")
    @Column(name = "Doctor_id", nullable = false)
    private int doctorId;

    @NotBlank(message = "User ID is required")
    @Column(name = "User_id", insertable = false, updatable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User notify;

    @PrePersist
    public void generateUUID() {
        if (this.notification_id == null || this.notification_id.isEmpty()) {
            this.notification_id = UUID.randomUUID().toString();
        }
    }
}
