package com.availabilitySchedule.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Availability {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "availability_id",nullable = false,unique = true)
    private String availabilityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot")
    private Timeslots timeSlots;

    @Column(name = "date", nullable= true)
    private Date date;

    @Column(name = "doctor_name", nullable = true)
    private String doctorName;

    @Column(name = "doctor_id", nullable = true)
    private String doctorId;
    
    @PrePersist
    protected void onCreate() {
    	if(availabilityId==null) {
    		availabilityId = UUID.randomUUID().toString();
    	}
    }
}