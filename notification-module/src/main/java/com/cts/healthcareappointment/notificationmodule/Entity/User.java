package com.cts.healthcareappointment.notificationmodule.Entity;


import lombok.Data;
import lombok.NoArgsConstructor;

//import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@JsonIgnore
	@Column(name="user_id")
	private String id;
	
	public void generateuuid() {
		this.id=UUID.randomUUID().toString();
	}
    
    @OneToOne(mappedBy = "doctor")
    private Doctor doctor;
    
    @OneToOne(mappedBy = "patient")
    private Patient patient;

	@OneToMany(mappedBy = "notify")
    @JsonBackReference
	private Set<Notification> notify = new HashSet<Notification>();
	
    @Column(name = "role",nullable = false)
    private int role;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;
    
}

