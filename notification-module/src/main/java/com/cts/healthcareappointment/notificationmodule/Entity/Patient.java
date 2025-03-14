package com.cts.healthcareappointment.notificationmodule.Entity;


//import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	@JsonIgnore
	private String Patient_id;
	
	@OneToOne
	@MapsId
	@JoinColumn(name="Patient_id")
	private User patient;
	
	
	//@OneToMany(mappedBy = "pat")
	//private Set<Appointment> appoint = new HashSet<Appointment>();
	
	
}
