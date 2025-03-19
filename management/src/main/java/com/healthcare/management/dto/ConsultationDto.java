package com.healthcare.management.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConsultationDto is a Data Transfer Object (DTO) that represents the details of a consultation.
 * It includes fields for consultation ID, appointment ID, notes, and prescription.
 * 
 * @Data - Generates getters, setters, toString, equals, and hashCode methods.
 * @NoArgsConstructor - Generates a no-argument constructor.
 * @AllArgsConstructor - Generates an all-argument constructor.
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDto {
    private String consultationId;
    
    @NotNull(message="Appointment ID cannot be null")
    private String appointmentId; 
    
    @Size(max = 500 , message="Notes cannot exceed more than 500 characters")
    private String notes;
    
    @NotNull(message="Prescription cannot be null")
    private String prescription;
}