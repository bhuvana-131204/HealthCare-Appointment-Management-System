package com.availabilitySchedule.controller;

import com.availabilitySchedule.dto.AvailabilityDTO;
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

    @PostMapping("/update")
    public ResponseEntity<Response<String>> updateAvailability(@RequestBody AvailabilityDTO availabilityDTO) {
        try {
            availabilityService.updateAvailability(availabilityDTO);
            Response<String> response = new Response<>(true, HttpStatus.OK, "Availability updated successfully");
            log.info("Exiting updateAvailability method");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DatabaseException e) {
            log.error("Error in updateAvailability method", e);
            Response<String> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/doctor/{doctorId}/{date}")
    public ResponseEntity<Response<List<Availability>>> getAvailabilityByDoctorIdAndDate(
            @PathVariable String doctorId, @PathVariable LocalDate date) {
        log.info("Entering getAvailability method for doctorId and date: {} {}", doctorId, date);
        try {
            List<Availability> availabilityList = availabilityService.getAvailabilityByDoctorIdAndDate(doctorId, date);
            Response<List<Availability>> response = new Response<>(true, HttpStatus.OK, availabilityList);
            log.info("Exiting getAvailability method for doctorId and date: {} {}", doctorId, date);
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
    public ResponseEntity<Response<List<Availability>>> getAvailabilityBySpecializationAndDate(@PathVariable Specialization specialization,@PathVariable LocalDate date) {
        log.info("Entering getAvailability method for specialization and date: {} {}", specialization, date);
        try {
            List<Availability> availabilities = availabilityService.getAvailabilityBySpecializationAndDate(specialization, date);
            Response<List<Availability>> response = new Response<>(true, HttpStatus.OK, availabilities);
            log.info("Exiting getAvailability method for specialization and date: {} {}", specialization, date);
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
        log.info("Entering blockTimeSlot method for availabilityId: {}", availabilityId);
        try {
            availabilityService.blockTimeSlot(availabilityId);
            Response<String> response = new Response<>(true, HttpStatus.OK, "Time slot blocked successfully");
            log.info("Exiting blockTimeSlot method for availabilityId: {}", availabilityId);
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
        log.info("Entering deleteAvailability method for availabilityId: {}", availabilityId);
        try {
            availabilityService.deleteAvailability(availabilityId);
            Response<String> response = new Response<>(true, HttpStatus.OK, "Availability deleted successfully");
            log.info("Exiting deleteAvailability method for availabilityId: {}", availabilityId);
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