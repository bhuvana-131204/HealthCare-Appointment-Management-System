package com.healthcare.management.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {
    private long historyId;
    
    @NotNull(message="Patient ID cannot be null")
    private String patientId; 
    
    @Size(max=500,message="Medical history do not exceed more than 500 characters")
    private String healthHistory;
}