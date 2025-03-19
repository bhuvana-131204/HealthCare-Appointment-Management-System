package com.availabilitySchedule.controller;

import com.availabilitySchedule.dto.Response;
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

/**
 * REST controller for managing availability.
 * 
 * @author Swapnil Rajesh
 * @since 18/02/2025
 */
@RestController
@RequestMapping("availability")
@Slf4j
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    /**
     * GET /AvailabilityId/{availabilityId} : Get availability by ID.
     *
     * @param availabilityId the ID of the availability
     * @return the ResponseEntity with status 200 (OK) and the availability, or status 404 (Not Found)
     */
    @GetMapping("/AvailabilityId/{availabilityId}")
    public ResponseEntity<Response<?>> viewById(@PathVariable String availabilityId) {
        log.info("Fetching availability for ID: {}", availabilityId);
        Availability availability = availabilityService.viewById(availabilityId);
        Response<?> response = new Response<>(true, HttpStatus.OK, availability);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * PUT /update/{availableId}/reschedule/{unavailableId} : Update availability status.
     *
     * @param availableId the ID of the availability to be marked as available
     * @param unavailableId the ID of the availability to be marked as unavailable
     * @return the ResponseEntity with status 200 (OK) and a success message
     */
    @PutMapping("/update/{availableId}/reschedule/{unavailableId}")
    public ResponseEntity<Response<?>> updateAvailability(@PathVariable String availableId,
                                                          @PathVariable String unavailableId) {
        log.info("Updating availability status: availableId={}, unavailableId={}", availableId, unavailableId);
        availabilityService.updateAvailabilityStatus(availableId, unavailableId);
        Response<?> response = new Response<>(true, HttpStatus.OK, "Availability updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET /doctor/{doctorId}/date/{date} : Get availability by doctor ID and date.
     *
     * @param doctorId the ID of the doctor
     * @param date the date of the availability
     * @return the ResponseEntity with status 200 (OK) and the list of availabilities, or status 404 (Not Found)
     */
    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<Response<List<?>>> getAvailabilityByDoctorIdAndDate(@PathVariable String doctorId,
                                                                              @PathVariable LocalDate date) {
        log.info("Fetching availability for doctorId={} and date={}", doctorId, date);
        List<Availability> availabilityList = availabilityService.getAvailabilityByDoctorIdAndDate(doctorId, date);
        Response<List<?>> response = new Response<>(true, HttpStatus.OK, availabilityList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET /specialization/{specialization}/date/{date} : Get availability by specialization and date.
     *
     * @param specialization the specialization of the doctor
     * @param date the date of the availability
     * @return the ResponseEntity with status 200 (OK) and the list of availabilities, or status 404 (Not Found)
     */
    @GetMapping("/specialization/{specialization}/date/{date}")
    public ResponseEntity<Response<List<?>>> getAvailabilityBySpecializationAndDate(
            @PathVariable Specialization specialization, @PathVariable LocalDate date) {
        log.info("Fetching availability for specialization={} and date={}", specialization, date);
        List<Availability> availabilities = availabilityService.getAvailabilityBySpecializationAndDate(specialization, date);
        Response<List<?>> response = new Response<>(true, HttpStatus.OK, availabilities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * GET /doctor/{doctorId}/date-range : Get availability by doctor ID and date range.
     * 
     * @param doctorId the ID of the doctor
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return the ResponseEntity with status 200 (OK) and the list of availabilities, or status 404 (Not Found)
     */
    @GetMapping("/doctor/{doctorId}/date-range")
    public ResponseEntity<Response<List<?>>> getAvailabilityByDoctorIdAndDateRange(
            @PathVariable String doctorId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Fetching availability for doctorId={}, startDate={}, endDate={}", doctorId, startDate, endDate);
        List<Availability> availabilities = availabilityService.getAvailabilityByDoctorIdAndDateRange(doctorId, startDate, endDate);
        Response<List<?>> response = new Response<>(true, HttpStatus.OK, availabilities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET /specialization/{specialization}/date-range : Get availability by specialization and date range.
     * 
     * @param specialization the specialization of the doctor
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return the ResponseEntity with status 200 (OK) and the list of availabilities, or status 404 (Not Found)
     */
    @GetMapping("/specialization/{specialization}/date-range")
    public ResponseEntity<Response<List<?>>> getAvailabilityBySpecializationAndDateRange(
            @PathVariable Specialization specialization,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Fetching availability for specialization={}, startDate={}, endDate={}", specialization, startDate, endDate);
        List<Availability> availabilities = availabilityService.getAvailabilityBySpecializationAndDateRange(specialization, startDate, endDate);
        Response<List<?>> response = new Response<>(true, HttpStatus.OK, availabilities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET /doctors : Get all availabilities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of all availabilities
     */
    @GetMapping("/doctors")
    public ResponseEntity<Response<List<?>>> viewAllAvailabilities() {
        log.info("Fetching all availabilities.");
        List<Availability> availabilities = availabilityService.viewAllAvailabilities();
        Response<List<?>> response = new Response<>(true, HttpStatus.OK, availabilities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * PUT /block/{availabilityId} : Block a time slot.
     *
     * @param availabilityId the ID of the availability to be blocked
     * @return the ResponseEntity with status 200 (OK) and a success message
     */
    @PutMapping("/block/{availabilityId}")
    public ResponseEntity<Response<?>> blockTimeSlot(@PathVariable String availabilityId) {
        log.info("Blocking time slot with ID: {}", availabilityId);
        availabilityService.blockTimeSlot(availabilityId);
        Response<?> response = new Response<>(true, HttpStatus.OK, "Time slot blocked successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * DELETE /delete/{availabilityId} : Delete an availability.
     *
     * @param availabilityId the ID of the availability to be deleted
     * @return the ResponseEntity with status 200 (OK) and a success message
     */
    @DeleteMapping("/delete/{availabilityId}")
    public ResponseEntity<Response<?>> deleteAvailability(@PathVariable String availabilityId) {
        log.info("Deleting availability with ID: {}", availabilityId);
        availabilityService.deleteAvailability(availabilityId);
        Response<?> response = new Response<>(true, HttpStatus.OK, "Availability deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * PUT /release/{availabilityId} : Release an availability.
     *
     * @param availabilityId the ID of the availability to be released
     * @return the ResponseEntity with status 200 (OK) and a success message
     */
    @PutMapping("/release/{availabilityId}")
    public ResponseEntity<Response<?>> releaseAvailabilityById(@PathVariable String availabilityId) {
        log.info("Releasing availability with ID: {}", availabilityId);
        availabilityService.releaseAvailabilityById(availabilityId);
        Response<?> response = new Response<>(true, HttpStatus.OK, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}