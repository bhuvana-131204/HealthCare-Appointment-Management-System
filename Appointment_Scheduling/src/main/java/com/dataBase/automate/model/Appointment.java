package com.dataBase.automate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="appointment_id", nullable = false, unique = true)
    private String appointmentId;
    @Enumerated(EnumType.STRING)
    @Column(name = "time_slot")
    private TimeSlots timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    
    
    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "doctor_id")
    private String doctorId;
    
    
    @Column(name = "date")
    private Date date;
    
    @Column(name = "availability_id")
    private String availabilityId;
	

	@PrePersist
	protected void onCreate() {
	    if (appointmentId == null) {
	        appointmentId = UUID.randomUUID().toString();
	    }
	}
}