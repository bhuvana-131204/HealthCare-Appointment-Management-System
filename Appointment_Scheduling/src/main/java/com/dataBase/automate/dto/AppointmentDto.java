package com.dataBase.automate.dto;

import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.dataBase.automate.model.Status;
import com.dataBase.automate.model.TimeSlots;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
	@NotEmpty
	private String appointmentId;
	@NotNull 
    private TimeSlots timeSlot;
	@NotNull
    private Status status;
	@NotEmpty
    private String doctorId;
    private LocalDate date;
    @NotEmpty
    private String availabilityId;
    @NotEmpty
    private String patientId;

   
   
	
}