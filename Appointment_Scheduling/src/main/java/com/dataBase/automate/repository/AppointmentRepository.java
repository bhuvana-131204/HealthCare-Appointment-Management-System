package com.dataBase.automate.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dataBase.automate.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

	
}