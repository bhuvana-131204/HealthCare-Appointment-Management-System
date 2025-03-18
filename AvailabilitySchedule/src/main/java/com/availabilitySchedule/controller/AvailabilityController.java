package com.availabilitySchedule.controller;

import com.availabilitySchedule.dto.Response;
import com.availabilitySchedule.exception.NoAvailabilityFoundException;
import com.availabilitySchedule.exception.DatabaseException;
import com.availabilitySchedule.service.AvailabilityService;
import com.availabilitySchedule.model.Availability;
import com.availabilitySchedule.model.Specialization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("availability")
@Slf4j
public class AvailabilityController {

	@Autowired
	private AvailabilityService availabilityService;

	@PostMapping("/update/{availableId}/{unavailableId}")
	public ResponseEntity<Response<String>> updateAvailability(@PathVariable String availableId,
			@PathVariable String unavailableId) {
		try {
			availabilityService.updateAvailabilityStatus(availableId, unavailableId);
			Response<String> response = new Response<>(true, HttpStatus.OK, "Availability updated successfully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DatabaseException e) {
			log.error("Error in updateAvailability method", e);
			Response<String> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/doctor/{doctorId}/{date}")
	public ResponseEntity<Response<List<Availability>>> getAvailabilityByDoctorIdAndDate(@PathVariable String doctorId,
			@PathVariable LocalDate date) {
		try {
			List<Availability> availabilityList = availabilityService.getAvailabilityByDoctorIdAndDate(doctorId, date);
			Response<List<Availability>> response = new Response<>(true, HttpStatus.OK, availabilityList);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoAvailabilityFoundException e) {
			log.error("Error in getAvailability method", e);
			Response<List<Availability>> response = new Response<>(false, HttpStatus.NOT_FOUND, null);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (DatabaseException e) {
			log.error("Error in getAvailability method", e);
			Response<List<Availability>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/specialization/{specialization}/{date}")
	public ResponseEntity<Response<List<Availability>>> getAvailabilityBySpecializationAndDate(
			@PathVariable Specialization specialization, @PathVariable LocalDate date) {
		try {
			List<Availability> availabilities = availabilityService
					.getAvailabilityBySpecializationAndDate(specialization, date);
			Response<List<Availability>> response = new Response<>(true, HttpStatus.OK, availabilities);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoAvailabilityFoundException e) {
			log.error("Error in getAvailability method", e);
			Response<List<Availability>> response = new Response<>(false, HttpStatus.NOT_FOUND, null);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (DatabaseException e) {
			log.error("Error in getAvailability method", e);
			Response<List<Availability>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/doctors")
	public ResponseEntity<List<Availability>> viewAllAvailabilities() {
		try {
			List<Availability> availabilities = availabilityService.viewAllAvailabilities();
			return new ResponseEntity<>(availabilities, HttpStatus.OK);
		} catch (DatabaseException e) {
			log.error("Error in viewAllAvailabilities method", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/block/{availabilityId}")
	public ResponseEntity<Response<String>> blockTimeSlot(@PathVariable String availabilityId) {
		try {
			availabilityService.blockTimeSlot(availabilityId);
			Response<String> response = new Response<>(true, HttpStatus.OK, "Time slot blocked successfully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoAvailabilityFoundException e) {
			log.error("Error in blockTimeSlot method", e);
			Response<String> response = new Response<>(false, HttpStatus.NOT_FOUND, e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (DatabaseException e) {
			log.error("Error in blockTimeSlot method", e);
			Response<String> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/delete/{availabilityId}")
	public ResponseEntity<Response<String>> deleteAvailability(@PathVariable String availabilityId) {
		try {
			availabilityService.deleteAvailability(availabilityId);
			Response<String> response = new Response<>(true, HttpStatus.OK, "Availability deleted successfully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoAvailabilityFoundException e) {
			log.error("Error in deleteAvailability method", e);
			Response<String> response = new Response<>(false, HttpStatus.NOT_FOUND, e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (DatabaseException e) {
			log.error("Error in deleteAvailability method", e);
			Response<String> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}