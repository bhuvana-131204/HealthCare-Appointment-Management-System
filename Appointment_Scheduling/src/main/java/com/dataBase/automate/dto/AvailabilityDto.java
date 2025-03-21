package com.dataBase.automate.dto;

import com.dataBase.automate.model.Specialization;
import com.dataBase.automate.model.TimeSlots;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.util.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityDto {
	@NotEmpty
    private String availabilityId;
	@NotNull
    private TimeSlots timeSlots;
	@NotNull
    private LocalDate date;
	@NotEmpty
    private String doctorId;
	@NotNull
    private Specialization specialization;
    
}
