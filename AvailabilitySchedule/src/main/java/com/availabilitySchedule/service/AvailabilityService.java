package com.availabilitySchedule.service;

import com.availabilitySchedule.dto.AvailabilityDTO;
import com.availabilitySchedule.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {
	@Autowired
    private AvailabilityRepository repository;

    public void updateAvailability(AvailabilityDTO availabilityDTO) {
        repository.save(availabilityDTO.toEntity());
    }

    public AvailabilityDTO getAvailability(Long doctorId) {
        return AvailabilityDTO.fromEntity(repository.findByDoctorId(doctorId));
    }
}
