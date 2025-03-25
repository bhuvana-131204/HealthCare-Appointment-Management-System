package com.healthcare.management.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
//import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
	@Id
	private String Patient_id;
	
	@OneToOne
	@MapsId
	@JoinColumn(name="Patient_id")
	private User patient;
	
	@Column(name ="Name",nullable = false)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "Gender",nullable = false)
	private Gender gender;
	
	@Column(name="Age",nullable = false)
	private int age;
	
	@Column(name="Address",nullable = false)
	private String address; 
	
	@OneToMany(mappedBy = "pat")
	private Set<Appointment> appoint = new HashSet<Appointment>();
	
	@OneToMany(mappedBy = "patientId")
	private Set<MedicalHistory> medicalHistory = new HashSet<MedicalHistory>();
	
}
