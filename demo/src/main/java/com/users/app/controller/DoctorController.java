package com.users.app.controller;

import java.util.Arrays;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.users.app.dto.DoctorDto;
import com.users.app.enums.Specialization;
import com.users.app.exceptions.DoctorNotFoundException;
import com.users.app.exceptions.EmailAlreadyExistsException;
import com.users.app.exceptions.PhoneNumberAlreadyExistsException;
import com.users.app.model.Doctor;
import com.users.app.service.DoctorService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
	@Autowired
	private DoctorService doctorService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getDoctor(@PathVariable String id) {
		try {
		DoctorDto doctor = doctorService.getDoctorById(id);
		return new ResponseEntity<>(doctor, HttpStatus.OK);
		}
		catch(DoctorNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
	@PatchMapping("/update")
	public ResponseEntity<String> updateDoctor(@RequestBody DoctorDto doc){
		
		try {
			doctorService.updateDoctorDetails(doc);
			return ResponseEntity.ok("Doctor details updated Successfully");
		}
		catch(DoctorNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
		catch(PhoneNumberAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
		catch(EmailAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
		
	}
	
	@GetMapping("/doctorSearch")
	public ResponseEntity<?> getDoctors(@RequestParam String specialization) {
	    try {
	        // Check if the specialization is present in the Specialization enum
	        boolean isValidSpecialization = Arrays.stream(Specialization.values())
	                                              .anyMatch(s -> s.name().equalsIgnoreCase(specialization));

	        if (!isValidSpecialization) {
	            throw new IllegalArgumentException("Invalid Specialization: " + specialization);
	        }

	        // Convert the string to the corresponding enum value
	        Specialization specEnum = Specialization.valueOf(specialization);

	        // Fetch doctors based on the specialization
	        List<Doctor> doctors = doctorService.getDoctorBySpecialization(specEnum);

	        // Return the list of doctors with an OK status
	        return new ResponseEntity<>(doctors, HttpStatus.OK);
	    } catch (DoctorNotFoundException e) {
	        // Handle case where no doctors are found
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	    } catch (IllegalArgumentException e) {
	        // Handle case where the specialization string does not match any enum value
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}
	
	@GetMapping("/alldoctors")
	public ResponseEntity<?> getAllDoctors(){
		try {
			List<Doctor> doctors = doctorService.getAllDoctors();
			return new ResponseEntity<>(doctors,HttpStatus.OK);
		}
		catch(DoctorNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
}
