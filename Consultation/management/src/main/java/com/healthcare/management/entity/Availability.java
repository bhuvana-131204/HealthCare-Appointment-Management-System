package com.healthcare.management.entity;

import java.sql.Date;
//import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Availability {
	
	@Id
	@Column(name="Availability_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long availability_id;
	
	@Column(name = "Slot")
	private Timeslots slots;
	
	@Column(name="Date")
	private Date date;
	
	@ManyToOne
	@JoinColumn(name="doctor_id")
	private Doctor doctor;
}

