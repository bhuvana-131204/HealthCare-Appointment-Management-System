package com.cts.healthcareappointment.notificationmodule.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Appointment_id",nullable =false ,unique = true)
	private int appointment_id;
	
	@ManyToOne
	@JoinColumn(name = "Doctor_id")
	private Doctor doc;
	
	@ManyToOne
	@JoinColumn(name="Patient_id")
	private Patient pat;
	
	@Column(name ="Status")
	private NotificationStatus status;
	
}
