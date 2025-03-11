package com.availabilitySchedule.dto;

import com.availabilitySchedule.model.Availability;
import com.availabilitySchedule.model.Timeslots;

import java.util.List;

public class AvailabilityDTO {
    private Long doctorId;
    private String date;
    private Timeslots timeSlots;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Timeslots getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(Timeslots timeslots2) {
        this.timeSlots = timeslots2;
    }

    // Convert DTO to Entity
    public Availability toEntity() {
        Availability availability = new Availability();
        availability.setDoctorId(this.doctorId);
        availability.setDate(java.sql.Date.valueOf(this.date)); // Assuming date is in 'yyyy-MM-dd' format
        availability.setSlots(this.timeSlots);
        return availability;
    }

    // Convert Entity to DTO
    public static AvailabilityDTO fromEntity(Availability availability) {
        AvailabilityDTO dto = new AvailabilityDTO();
        dto.setDoctorId(availability.getDoctorId());
        dto.setDate(availability.getDate().toString());
        dto.setTimeSlots(availability.getSlots());
        return dto;
    }
}