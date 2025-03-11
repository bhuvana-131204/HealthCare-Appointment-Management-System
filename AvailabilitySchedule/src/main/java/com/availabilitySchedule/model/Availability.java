package com.availabilitySchedule.model;

import java.sql.Date;
//import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id")
    private long availabilityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot", nullable = false)
    private Timeslots slots;

    @Column(name = "date", nullable = false)
    private Date date;

    
    @Column(name = "doctor_name", nullable = true)
    private String doctorName;

    @Column(name = "doctor_id", nullable = true)
    private Long doctorId; 

	
}
