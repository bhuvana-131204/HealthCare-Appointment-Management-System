package com.cts.healthcareappointment.notificationmodule.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String id;

   @OneToMany(mappedBy = "notify") // Correct mappedBy field
    private Set<Notification> notifications;
    
}
