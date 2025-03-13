package com.dataBase.automate.repository;

import com.dataBase.automate.model.Availability;
import com.dataBase.automate.model.Specialization;
import com.dataBase.automate.model.TimeSlots;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, String> {
	List<Availability> findByDate(Date date);
    List<Availability> findByDateAndSlots(Date date, TimeSlots slots);
    List<Availability> findByDateAndSpecialization(Date date, Specialization specialization);
   
    List<Availability> findByDateAndSlotsAndSpecialization(Date date, TimeSlots slots, Specialization specialization);
}