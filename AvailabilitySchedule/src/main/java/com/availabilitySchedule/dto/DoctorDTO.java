package com.availabilitySchedule.dto;


import com.availabilitySchedule.model.Specialization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
	
	private String id;
	
	private String name;
	
	private Specialization specialization;
}
