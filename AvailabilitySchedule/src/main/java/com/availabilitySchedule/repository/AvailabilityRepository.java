package com.availabilitySchedule.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import com.availabilitySchedule.model.Availability;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, String> {
    Availability findByDoctorIdAndDate(String doctorId, Date date);
    Availability findByDoctorId(String doctorId);
}