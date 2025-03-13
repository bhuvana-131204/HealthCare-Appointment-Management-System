package com.dataBase.automate.dto;

import java.util.Date;

import com.dataBase.automate.model.Status;
import com.dataBase.automate.model.TimeSlots;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
	private String appointmentId;
    private TimeSlots timeSlot;
    private Status status;
    private String doctorId;
    private Date date;
   
   
	
}