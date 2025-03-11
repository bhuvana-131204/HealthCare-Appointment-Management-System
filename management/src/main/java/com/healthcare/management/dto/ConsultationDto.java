package com.healthcare.management.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDto {
    private int consultationId;
    
    @NotNull
    private int appointmentId; 
    
    @Size(max = 500 , message="Notes cannot exceed more than 500 characters")
    private String notes;
    
    @NotNull
    private String prescription;
}