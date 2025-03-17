package com.availabilitySchedule.model;

import java.sql.Date;

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
