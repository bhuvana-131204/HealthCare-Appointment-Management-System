package com.dataBase.automate.dto;


import javax.validation.constraints.NotNull;

import javax.validation.constraints.Size;
 
import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;
 
@Data

@NoArgsConstructor

@AllArgsConstructor

public class ConsultationDto {

    private String consultationId;

    @NotNull

    private String appointmentId; 

    @Size(max = 500 , message="Notes cannot exceed more than 500 characters")

    private String notes;

    
    private String prescription;

}
 