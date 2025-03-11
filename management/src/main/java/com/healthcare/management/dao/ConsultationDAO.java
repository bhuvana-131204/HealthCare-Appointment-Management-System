package com.healthcare.management.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthcare.management.entity.Consultation;

public interface ConsultationDAO extends JpaRepository<Consultation, Integer> {
	
//	@Query(name="findConsultationDetailsByAppointmentId")
//	public List<Consultation> findConsultationDetailsByAppointmentId(int appointment_id);
	
	@Query("SELECT c from Consultation c where c.appointment.appointment_id=:appointment_Id")
	public List<Consultation> findConsultationDetailsByAppointmentId(@Param("appointment_Id") int appointment_Id);
	
	Optional<Consultation>findByConsultationId(int consultationId);
	
}	
