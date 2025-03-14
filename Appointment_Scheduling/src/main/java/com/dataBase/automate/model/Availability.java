package com.dataBase.automate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

import java.util.Date;

@Entity
//@Data
@NoArgsConstructor
@AllArgsConstructor
public class Availability {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="availability_id")
    private String availabilityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
    private Specialization specialization;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "slot")
    private TimeSlots slots;

    @Column(name="date")
    private Date date;

    @Column(name="doctor_id")
    private String doctorId;
    @PrePersist
    protected void onCreate() {
        if (availabilityId == null) {
            availabilityId = UUID.randomUUID().toString();
        }
    }
	public String getAvailabilityId() {
		return availabilityId;
	}
	public void setAvailabilityId(String availabilityId) {
		this.availabilityId = availabilityId;
	}
	public Specialization getSpecialization() {
		return specialization;
	}
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}
	public TimeSlots getSlots() {
		return slots;
	}
	public void setSlots(TimeSlots slots) {
		this.slots = slots;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
    
 
}