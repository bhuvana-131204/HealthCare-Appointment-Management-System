package com.availabilitySchedule.service;

import com.availabilitySchedule.dto.AvailabilityDTO;
import com.availabilitySchedule.exception.NoAvailabilityFoundException;
import com.availabilitySchedule.model.Availability;
import com.availabilitySchedule.repository.AvailabilityRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;



@Service
@Slf4j
public class AppointmentService {


    @Autowired
    private AvailabilityRepository repository;

    public void blockTimeSlot(AvailabilityDTO availabilityDTO) {
        Availability availability = repository.findByDoctorIdAndDate(availabilityDTO.getDoctorId(), Date.valueOf(availabilityDTO.getDate()));
        if (availability != null) {
            availability.setTimeSlots(availabilityDTO.getTimeSlots());
            repository.save(availability);
            log.info("Blocked time slot for doctorId: {} on date: {}", availability.getDoctorId(), availability.getDate());
        } else {
            log.error("No availability found for doctorId: {} on date: {}", availabilityDTO.getDoctorId(), availabilityDTO.getDate());
            throw new NoAvailabilityFoundException("No availability found for the specified doctor and date");
        }
    }
}