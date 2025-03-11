package com.healthcare.management.dao;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthcare.management.entity.MedicalHistory;

public interface HistoryDAO extends JpaRepository<MedicalHistory, Long>{
	
	@Query("SELECT h FROM MedicalHistory h WHERE h.patientId.Patient_id = :patientId")
	public MedicalHistory getMedicalHistoryByPatientId(@Param("patientId") String Patient_id);
}
