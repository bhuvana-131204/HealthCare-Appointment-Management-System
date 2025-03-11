package com.healthcare.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name="getMedicalHistoryByPatientId",
query="SELECT h FROM MedicalHistory h WHERE h.patientId.Patient_id = :patientId")
public class MedicalHistory {
	
	@Id
	@Column(name="History_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long history_id;	
	
	@ManyToOne
	@JoinColumn(name="Patient_id")
	private Patient patientId;
	//private Patient medicalHistory;
	
	
	@Column(name="DiseaseHistory")
	private String healthHistory;
	
}
