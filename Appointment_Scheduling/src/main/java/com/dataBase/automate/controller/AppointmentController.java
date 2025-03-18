package com.dataBase.automate.controller;
import com.dataBase.automate.dto.AppointmentDto;
import com.dataBase.automate.dto.AvailabilityDto;
import com.dataBase.automate.dto.Response;
import com.dataBase.automate.exception.AppointmentNotFoundException;
import com.dataBase.automate.exception.AvailabilityConflictException;
import com.dataBase.automate.exception.AvailabilityNotFoundException;
import com.dataBase.automate.exception.SpecializationNotFoundException;
import com.dataBase.automate.exception.TimeSlotNotFoundException;
import com.dataBase.automate.model.Appointment;
import com.dataBase.automate.model.Availability;
import com.dataBase.automate.model.Specialization;
import com.dataBase.automate.model.TimeSlots;
import com.dataBase.automate.service.AppointmentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing appointments.
 * 
 * @Author Sanjay R
 * @Since 2025-03-18
 */
@Slf4j
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * Creates a new appointment.
     * 
     * @param availabilityId the ID of the availability
     * @return ResponseEntity containing the created appointment
     */
    @PostMapping("/create/{availabilityId}")
    public ResponseEntity<Response<Appointment>> createAppointment(@PathVariable String availabilityId) {
        try {
            Appointment appointment = appointmentService.createAppointment(availabilityId);
            Response<Appointment> response = new Response<>(true, HttpStatus.CREATED, appointment, null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AvailabilityNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<Appointment> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Views all appointments.
     * 
     * @return ResponseEntity containing the list of appointments
     */
    @GetMapping("/view")
    public ResponseEntity<Response<List<Appointment>>> viewAppointments() {
        try {
            List<Appointment> appointments = appointmentService.viewAppointments();
            Response<List<Appointment>> response = new Response<>(true, HttpStatus.OK, appointments, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<Appointment>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Fetches an appointment by ID.
     * 
     * @param id the ID of the appointment
     * @return ResponseEntity containing the appointment
     */
    @GetMapping("/view/{id}")
    public ResponseEntity<Response<Appointment>> fetchAppointmentById(@PathVariable String id) {
        try {
            Appointment appointment = appointmentService.fetchAppointmentById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found for ID: " + id));
            Response<Appointment> response = new Response<>(true, HttpStatus.OK, appointment, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<Appointment> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates an appointment by ID and availability ID.
     * 
     * @param id the ID of the appointment
     * @param availabilityId the new availability ID
     * @return ResponseEntity containing the updated appointment
     */
    @PutMapping("/update/{id}/{availabilityId}")
    public ResponseEntity<Response<Appointment>> updateAppointment(@PathVariable String id, @PathVariable String availabilityId) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(id, availabilityId);
            Response<Appointment> response = new Response<>(true, HttpStatus.OK, updatedAppointment, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<Appointment> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (AvailabilityConflictException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<Appointment> response = new Response<>(false, HttpStatus.CONFLICT, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    /**
     * Cancels an appointment by ID.
     * 
     * @param id the ID of the appointment
     * @return ResponseEntity indicating the cancellation status
     */
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Response<Void>> cancelAppointment(@PathVariable String id) {
        try {
            Appointment appointment = appointmentService.fetchAppointmentById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found for ID: " + id));
            appointmentService.cancelAppointment(id);
            appointmentService.notifyDoctorForRemoval(appointment.getDoctorId(), appointment.getPatientId(), id);
            Response<Void> response = new Response<>(true, HttpStatus.NO_CONTENT, null, null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (AppointmentNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<Void> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Fetches appointments by patient ID.
     * 
     * @param patientId the ID of the patient
     * @return ResponseEntity containing the list of appointments
     */
    @GetMapping("/viewByPatient/{patientId}")
    public ResponseEntity<Response<List<Appointment>>> fetchAppointmentsByPatientId(@PathVariable String patientId) {
        try {
            List<Appointment> appointments = appointmentService.fetchAppointmentsByPatientId(patientId);
            Response<List<Appointment>> response = new Response<>(true, HttpStatus.OK, appointments, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<Appointment>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Fetches appointments by doctor ID.
     * 
     * @param doctorId the ID of the doctor
     * @return ResponseEntity containing the list of appointments
     */
    @GetMapping("/viewByDoctor/{doctorId}")
    public ResponseEntity<Response<List<Appointment>>> fetchAppointmentsByDoctorId(@PathVariable String doctorId) {
        try {
            List<Appointment> appointments = appointmentService.fetchAppointmentsByDoctorId(doctorId);
            Response<List<Appointment>> response = new Response<>(true, HttpStatus.OK, appointments, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<Appointment>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets available doctors by date and specialization.
     * 
     * @param date the date
     * @param specialization the specialization
     * @return ResponseEntity containing the list of available doctors
     */
    @GetMapping("/dateAndSpecialization/{date}/{specialization}")
    public ResponseEntity<Response<List<String>>> getAvailableDoctorsByDateAndSpecialization(@PathVariable String date, @PathVariable String specialization) {
        try {
            List<String> availableDoctors = appointmentService.getAvailableDoctorsByDateAndSpecialization(date, specialization);
            Response<List<String>> response = new Response<>(true, HttpStatus.OK, availableDoctors, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<String>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets available doctors by date and doctor ID.
     * 
     * @param date the date
     * @param doctorId the ID of the doctor
     * @return ResponseEntity containing the list of available doctors
     */
    @GetMapping("/dateAndId/{date}/{doctorId}")
    public ResponseEntity<Response<List<String>>> getAvailableDoctorsByDateAndId(@PathVariable String date, @PathVariable String doctorId) {
        try {
            List<String> availableDoctors = appointmentService.getAvailableDoctorsByDateAndId(date, doctorId);
            Response<List<String>> response = new Response<>(true, HttpStatus.OK, availableDoctors, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<String>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles appointment notification.
     * 
     * @param appointmentId the ID of the appointment
     * @return ResponseEntity indicating the notification status
     */
    @PutMapping("/CancelledNotification/{appointmentId}")
    public ResponseEntity<Response<Void>> AppointmentNotification(@PathVariable String appointmentId) {
        try {
            appointmentService.updationAfterNotification(appointmentId);
            Response<Void> response = new Response<>(true, HttpStatus.OK, null, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<Void> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<Void> response = new Response<>(false, HttpStatus.BAD_REQUEST, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets available doctors.
     * 
     * @return ResponseEntity containing the list of available doctors
     */
    @GetMapping("/availableDoctors")
    public ResponseEntity<Response<List<AvailabilityDto>>> getAvailableDoctors() {
        try {
            List<AvailabilityDto> availableDoctors = appointmentService.getAvailableDoctors();
            Response<List<AvailabilityDto>> response = new Response<>(true, HttpStatus.OK, availableDoctors, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<AvailabilityDto>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   
    
    
  
    /**
    * Sets one availability
    * 
    * @param availability DTO
    * @return ResponseEntity indicating the status of the operation
    */
  @PostMapping("/setDoctor")
  public ResponseEntity<Response<Void>> setAvailableDoctor(@RequestBody AvailabilityDto availabilityDto) {
      try {
          appointmentService.setAvailableDoctor(availabilityDto);
          Response<Void> response = new Response<>(true, HttpStatus.OK, null, null);
          return new ResponseEntity<>(response, HttpStatus.OK);
      } catch (Exception ex) {
          log.error("Exception: {}", ex.getMessage());
          Response<Void> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
          return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

    /**
     * Shows availability of doctors.
     * 
     * @return ResponseEntity containing the list of doctor time slots
     */
    @GetMapping("/showAvailability")
    public ResponseEntity<Response<List<String>>> showAvailability() {
        try {
            List<String> doctorTimeSlots = appointmentService.showAvailability();
            Response<List<String>> response = new Response<>(true, HttpStatus.OK, doctorTimeSlots, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<String>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Notifies completion of an appointment.
     * 
     * @param appointmentId the ID of the appointment
     * @return ResponseEntity indicating the notification status
     */
    @PutMapping("/notifyCompletion/{appointmentId}")
    public ResponseEntity<Response<Void>> notifyCompletion(@PathVariable String appointmentId) {
        try {
            appointmentService.notifyAfterCompletion(appointmentId);
            Response<Void> response = new Response<>(true, HttpStatus.OK, null, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<Void> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<Void> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Exception Handlers

    /**
     * Handles AppointmentNotFoundException.
     * 
     * @param ex the exception
     * @return ResponseEntity containing the error message
     */
    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<Response<String>> handleAppointmentNotFoundException(AppointmentNotFoundException ex) {
        log.error("Exception: {}", ex.getMessage());
        Response<String> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles AvailabilityNotFoundException.
     * 
     * @param ex the exception
     * @return ResponseEntity containing the error message
     */
    @ExceptionHandler(AvailabilityNotFoundException.class)
    public ResponseEntity<Response<String>> handleAvailabilityNotFoundException(AvailabilityNotFoundException ex) {
        log.error("Exception: {}", ex.getMessage());
        Response<String> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}