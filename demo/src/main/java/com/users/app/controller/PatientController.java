package com.users.app.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.users.app.dto.PatientDto;
import com.users.app.enums.Specialization;
import com.users.app.exceptions.DoctorNotFoundException;
import com.users.app.exceptions.EmailAlreadyExistsException;
import com.users.app.exceptions.PhoneNumberAlreadyExistsException;
import com.users.app.exceptions.UserNotFoundException;
import com.users.app.model.Doctor;
import com.users.app.service.PatientService;

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
	public ResponseEntity<?> getPatient(@PathVariable String id){
		try {
		PatientDto patient = patientService.getPatientById(id);
		return new ResponseEntity<>(patient,HttpStatus.OK);
		}
		catch(UserNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
	@PatchMapping("/update")
	public ResponseEntity<String> updatePatient(@RequestBody PatientDto pat){
		
		try {
			patientService.updatePatientDetails(pat);
			return ResponseEntity.ok("Patient Details Updated Successfully");
		}
		catch(UserNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
		catch(PhoneNumberAlreadyExistsException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
		catch(EmailAlreadyExistsException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	
}
