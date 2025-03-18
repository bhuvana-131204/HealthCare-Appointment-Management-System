package com.availabilitySchedule.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import com.availabilitySchedule.model.Availability;
import java.util.List;
import com.availabilitySchedule.model.Specialization;


@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, String> {
    List<Availability> findByDoctorIdAndDate(String doctorId, LocalDate date);
    Availability findByDoctorId(String doctorId);
    List<Availability> findBySpecializationAndDate(Specialization specialization, LocalDate date);
}