package com.availabilitySchedule.availabilityRepository;

import org.springframework.stereotype.Repository;

import com.availabilitySchedule.model.Availability;

import org.springframework.data.jpa.repository.*;

@Repository
public interface AvailabilityRepository extends JpaRepository <Availability, Long>{

}
