package com.availabilitySchedule.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity representing availability.
 * 
 * @author Swapnil Rajesh
 * @since 18/02/2025
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Availability {

	@Id
	@Column(name = "availability_id", nullable = false, unique = true)
	private String availabilityId;

	@Column(name = "doctor_id", nullable = true)
	private String doctorId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "specialization", nullable = true)
	private Specialization specialization;
	
	@Column(name = "date", nullable = true)
	private LocalDate date;

	@Enumerated(EnumType.STRING)
	@Column(name = "slot")
	private Timeslots timeSlots;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private Status status;
	
	@PrePersist
	protected void onCreate() {
		if (availabilityId == null) {
			availabilityId = UUID.randomUUID().toString();
		}
	}
}