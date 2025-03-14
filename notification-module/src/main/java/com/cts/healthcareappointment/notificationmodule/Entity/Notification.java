package com.cts.healthcareappointment.notificationmodule.Entity;

import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

	 @Id
	    @Column(name = "Notification_id", nullable = false, unique = true)
	   // @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private String notification_id;
	    
	    @PrePersist
	    public void generateUUID() {
	        this.notification_id = UUID.randomUUID().toString();
	    }

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private NotificationType type;

   @NotNull
    @Column(name = "Message", nullable = false)
    private String message;

    @Column(name = "SeenStatus", nullable = false)
    private boolean seenStatus = false; // Default value for unseen notifications

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @Column(name = "Doctor_id", nullable = false)
    private int doctorId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User notify;

    // Optional: @Transient can be used here if userId is only for computed operations
    @Transient
    private String userId; // Computed, not persisted

    // Lombok will generate these, but if needed, you can provide your own:
    public NotificationStatus getStatus() {
        return status;
    }

    public int getDoctorId() {
        return doctorId;
    }
}