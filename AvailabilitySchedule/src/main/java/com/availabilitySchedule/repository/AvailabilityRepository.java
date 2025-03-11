package com.availabilitySchedule.repository;

import org.springframework.stereotype.Repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

//import java.util.List;

import com.availabilitySchedule.model.Availability;

@Repository
public interface AvailabilityRepository extends JpaRepository <Availability, Long>{
	Availability findByDoctorIdAndDate(Long doctorId, Date date);
    Availability findByDoctorId(Long doctorId);
}
