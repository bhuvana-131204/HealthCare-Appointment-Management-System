package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.DoctorDto;
import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.exceptions.PhoneNumberAlreadyExistsException;
import com.example.demo.model.Doctor;
import com.example.demo.service.DoctorService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
	@Autowired
	private DoctorService doctorService;
	
	@GetMapping("/{id}")
	public DoctorDto getDoctor(@PathVariable String id) {
		DoctorDto doctor = doctorService.getDoctorById(id);
		return doctor;
	}
	
	@PatchMapping("/update")
	public ResponseEntity<String> updateDoctor(@RequestBody DoctorDto doc){
		
		try {
			doctorService.updateDoctorDetails(doc);
			return ResponseEntity.ok("Doctor details updated Successfully");
		}
		catch(PhoneNumberAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
		catch(EmailAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
		
	}
}
