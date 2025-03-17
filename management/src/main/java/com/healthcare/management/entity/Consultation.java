package com.healthcare.management.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
//import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@NamedQuery(name="findConsultationDetailsByAppointmentId", query="SELECT c from Consultation c where c.appointment.appointment_id=:appointmentId")
public class Consultation {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name="Consultation_id" ,nullable = false)
		private int consultationId; 
		
		@OneToOne(cascade=CascadeType.PERSIST)
		@JoinColumn(name = "Appointment_id")
		
		private Appointment appointment;
		
		@Column(name = "Notes")
		@Size(max = 500)
		private String notes; 
		
		@Column(name = "Prescription" , nullable = false)
		@NotNull
		private String prescription;
		
		public void setAppointment(Appointment appointment) {
	        this.appointment = appointment;
	    }
}
