package com.availabilitySchedule.dto;

import com.availabilitySchedule.model.Availability;
import com.availabilitySchedule.model.Timeslots;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityDTO {
    private String doctorId;
    private String date;
    private Timeslots timeSlots;
    private String doctorName;

    public Availability toEntity() {
        Availability availability = new Availability();
        availability.setDoctorId(this.doctorId);
        availability.setDate(Date.valueOf(this.date));
        availability.setTimeSlots(this.timeSlots);
        availability.setDoctorName(this.doctorName);
        return availability;
    }

    public static AvailabilityDTO fromEntity(Availability availability) {
        AvailabilityDTO dto = new AvailabilityDTO();
        dto.setDoctorId(availability.getDoctorId());
        dto.setDate(availability.getDate().toString());
        dto.setTimeSlots(availability.getTimeSlots());
        dto.setDoctorName(availability.getDoctorName());
        return dto;
    }
}