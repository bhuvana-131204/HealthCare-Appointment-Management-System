package com.dataBase.automate.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

/**
* Appointment Table.
* 
* @Author Sanjay R
* @Since 2025-03-18
*/

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    
    @Column(name="appointment_id", nullable = false, unique = true)
    private String appointmentId;
    @Enumerated(EnumType.STRING)
    @Column(name = "time_slot")
    private TimeSlots timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    
    
    @Column(name = "patient_id",nullable = false)
    private String patientId;

    @Column(name = "doctor_id" , nullable = false)
    private String doctorId;
    
    
    @Column(name = "date",nullable = false)
    private LocalDate date;
    
    @Column(name = "availability_id",nullable = false,unique=true)
    private String availabilityId;
	

	@PrePersist
	protected void onCreate() {
	    if (appointmentId == null) {
	        appointmentId = UUID.randomUUID().toString();
	    }
	}
}