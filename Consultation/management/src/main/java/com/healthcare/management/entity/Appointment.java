package com.healthcare.management.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Appointment_id",nullable =false ,unique = true)
	private String appointment_id;
	
	@ManyToOne
	@JoinColumn(name = "Doctor_id")
	private Doctor doc;
	
	@ManyToOne
	@JoinColumn(name="Patient_id")
	private Patient pat;
	
	@Enumerated(EnumType.STRING)
	@Column(name ="TimeSlot")
	private Timeslots time;
	
	@Enumerated(EnumType.STRING)
	@Column(name ="Status")
	private Status status;
	
}
