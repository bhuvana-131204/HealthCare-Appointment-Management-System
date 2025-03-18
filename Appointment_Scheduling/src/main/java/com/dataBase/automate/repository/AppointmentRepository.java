package com.dataBase.automate.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dataBase.automate.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

	Optional<Appointment> findByAvailabilityId(String availabilityId);

	List<Appointment> findByPatientId(String patientId);

	List<Appointment> findByDoctorId(String doctorId);
	

	
}