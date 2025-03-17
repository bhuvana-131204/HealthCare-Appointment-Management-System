package com.availabilitySchedule.dto;

import com.availabilitySchedule.model.Availability;
import com.availabilitySchedule.model.Specialization;
import com.availabilitySchedule.model.Timeslots;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityDTO {
    private String doctorId;
    private String doctorName;
    private Specialization specialization;
    private LocalDate date;
    private Timeslots timeSlots;

    public Availability toEntity() {
        Availability availability = new Availability();
        availability.setDoctorId(this.doctorId);
        availability.setDoctorName(this.doctorName);
        availability.setSpecialization(this.specialization);
        availability.setDate(this.date);
        availability.setTimeSlots(this.timeSlots);
        
        return availability;
    }

    public static AvailabilityDTO fromEntity(Availability availability) {
        AvailabilityDTO dto = new AvailabilityDTO();
        dto.setDoctorId(availability.getDoctorId());
        dto.setDate(availability.getDate());
        dto.setTimeSlots(availability.getTimeSlots());
        dto.setDoctorName(availability.getDoctorName());
        dto.setSpecialization(availability.getSpecialization());
        return dto;
    }
}