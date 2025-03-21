package com.availabilitySchedule.dto;

import com.availabilitySchedule.model.Specialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Doctor.
 * 
 * @author Swapnil Rajesh
 * @since 18/02/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAvailabilityDto {
	private String doctorId;
	private Specialization specialization;
}