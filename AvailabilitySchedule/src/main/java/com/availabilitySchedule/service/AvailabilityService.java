package com.availabilitySchedule.service;

import com.availabilitySchedule.dto.AvailabilityDTO;
import com.availabilitySchedule.exception.NoAvailabilityFoundException;
import com.availabilitySchedule.exception.DatabaseException;
import com.availabilitySchedule.model.Availability;
import com.availabilitySchedule.repository.AvailabilityRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@Slf4j
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository repository;

    public void updateAvailability(AvailabilityDTO availabilityDTO) throws DatabaseException {
        Availability availability = availabilityDTO.toEntity();
        availability.setTimeSlots(availabilityDTO.getTimeSlots());
        repository.save(availability);
        log.info("Updated availability for doctorId: {}", availability.getDoctorId());
    }

    public AvailabilityDTO getAvailability(String doctorId) throws NoAvailabilityFoundException, DatabaseException {
        Availability availability = repository.findByDoctorId(doctorId);
        if (availability == null) {
            log.error("No availability found for doctorId: {}", doctorId);
            throw new NoAvailabilityFoundException("No availability found for the specified doctor");
        }
        log.info("Fetched availability for doctorId: {}", doctorId);
        return AvailabilityDTO.fromEntity(availability);
    }

    public void blockTimeSlot(AvailabilityDTO availabilityDTO) throws NoAvailabilityFoundException, DatabaseException {
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

    public List<Availability> viewAllAvailabilities() {
        return repository.findAll();
    }

    public void deleteAvailability(String availabilityId) throws NoAvailabilityFoundException, DatabaseException {
        repository.deleteById(availabilityId);
    }
}