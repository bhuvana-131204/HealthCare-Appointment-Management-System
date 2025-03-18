package com.availabilitySchedule.dto;

import com.availabilitySchedule.model.Availability;
import com.availabilitySchedule.model.Specialization;
import com.availabilitySchedule.model.Status;
import com.availabilitySchedule.model.Timeslots;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    //private Status status;

    public Availability toEntity() {
        Availability availability = new Availability();
        availability.setDoctorId(this.doctorId);
        availability.setDoctorName(this.doctorName);
        availability.setSpecialization(this.specialization);
        availability.setDate(this.date);
        availability.setTimeSlots(this.timeSlots);
        availability.setStatus(Status.Available);
        
        return availability;
    }

    public static AvailabilityDTO fromEntity(Availability availability) {
        AvailabilityDTO dto = new AvailabilityDTO();
        dto.setDoctorId(availability.getDoctorId());
        dto.setDoctorName(availability.getDoctorName());
        dto.setSpecialization(availability.getSpecialization());
        dto.setDate(availability.getDate());
        dto.setTimeSlots(availability.getTimeSlots());        
        //dto.setStatus(availability.getStatus());
        return dto;
    }
}