package com.example.demo.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.PatientDto;
import com.example.demo.enums.Specialization;
import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.exceptions.NoDoctorFoundException;
import com.example.demo.exceptions.PhoneNumberAlreadyExistsException;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.service.PatientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/patient")
public class PatientController {		
		
	@Autowired
	private PatientService patientService;
	
	@GetMapping("/{id}")
	public PatientDto getPatient(@PathVariable String id){
		PatientDto patient = patientService.getPatientById(id);
		return patient;
	}
	
	@PatchMapping("/update")
	public ResponseEntity<String> updatePatient(@RequestBody PatientDto pat){
		
		try {
			patientService.updatePatientDetails(pat);
			return ResponseEntity.ok("Patient Details Updated Successfully");
		}
		catch(PhoneNumberAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
		catch(EmailAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@GetMapping("/doctorSearch")
	public ResponseEntity<?> getDoctors(@RequestParam String specialization){
		try {
			Specialization specEnum = Specialization.valueOf(specialization);
			 List<Doctor> doctors = patientService.getDoctorBySpecialization(specEnum);
		        return new ResponseEntity<>(doctors, HttpStatus.OK);
		}
		catch(NoDoctorFoundException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
}
