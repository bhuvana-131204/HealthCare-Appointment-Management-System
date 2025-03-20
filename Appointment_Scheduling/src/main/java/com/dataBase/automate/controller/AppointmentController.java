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
//import com.dataBase.automate.model.Availability;
import com.dataBase.automate.model.Specialization;
import com.dataBase.automate.model.TimeSlots;
import com.dataBase.automate.service.AppointmentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @GetMapping("/create/{availabilityId}")
    public ResponseEntity<Response<?>> createAppointment(@PathVariable String availabilityId) {
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
    public ResponseEntity<Response<List<?>>> viewAppointments() {
        try {
            List<Appointment> appointments = appointmentService.viewAppointments();
            Response<List<?>> response = new Response<>(true, HttpStatus.OK, appointments, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<?>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
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
    public ResponseEntity<Response<?>> fetchAppointmentById(@PathVariable String id) {
        try {
            Appointment appointment = appointmentService.fetchAppointmentById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found for ID: " + id));
            Response<?> response = new Response<>(true, HttpStatus.OK, appointment, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates an appointment by ID and availability ID.
     * 
     * @param appointmentId the ID of the appointment
     * @param availabilityId the new availability ID
     * @return ResponseEntity containing the updated appointment
     */
    @PutMapping("/update/{appointmentId}/{availabilityId}")
    public ResponseEntity<Response<?>> updateAppointment(@PathVariable String appointmentId, @PathVariable String availabilityId) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(appointmentId, availabilityId);
            Response<Appointment> response = new Response<>(true, HttpStatus.OK, updatedAppointment, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (AvailabilityConflictException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<?> response = new Response<>(false, HttpStatus.CONFLICT, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    /**
     * Cancels an appointment by ID.
     * 
     * @param id the ID of the appointment
     * @return ResponseEntity indicating the cancellation status
     */
    @DeleteMapping("/cancel/{AppointmentId}")
    public ResponseEntity<Response<?>> cancelAppointment(@PathVariable String AppointmentId) {
        try {
            appointmentService.fetchAppointmentById(AppointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found for ID: " + AppointmentId));
            appointmentService.cancelAppointment(AppointmentId);
           
            Response<?> response = new Response<>(true, HttpStatus.ACCEPTED, null, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
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
    public ResponseEntity<Response<List<?>>> fetchAppointmentsByPatientId(@PathVariable String patientId) {
        try {
            List<Appointment> appointments = appointmentService.fetchAppointmentsByPatientId(patientId);
            Response<List<?>> response = new Response<>(true, HttpStatus.OK, appointments, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<?>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
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
    public ResponseEntity<Response<List<?>>> fetchAppointmentsByDoctorId(@PathVariable String doctorId) {
        try {
            List<Appointment> appointments = appointmentService.fetchAppointmentsByDoctorId(doctorId);
            Response<List<?>> response = new Response<>(true, HttpStatus.OK, appointments, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<?>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
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
    public ResponseEntity<Response<List<?>>> getAvailableDoctorsByDateAndSpecialization(@PathVariable LocalDate date, @PathVariable Specialization specialization) {
        try {
        	List<AvailabilityDto> availableDoctors = appointmentService.getAvailableDoctorsByDateAndSpecialization(date, specialization);
            Response<List<?>> response = new Response<>(true, HttpStatus.OK, availableDoctors, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
        	ex.printStackTrace();
            log.error("Exception: {}", ex.getMessage());
            Response<List<?>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
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
    public ResponseEntity<Response<List<?>>> getAvailableDoctorsByDateAndId(@PathVariable LocalDate date, @PathVariable String doctorId) {
        try {
        	List<AvailabilityDto> availableDoctors = appointmentService.getAvailableDoctorsByDateAndId(date, doctorId);
            Response<List<?>> response = new Response<>(true, HttpStatus.OK, availableDoctors, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<List<?>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
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
    public ResponseEntity<Response<?>> AppointmentNotification(@PathVariable String appointmentId) {
        try {
            appointmentService.updationAfterNotification(appointmentId);
            Response<?> response = new Response<>(true, HttpStatus.OK, null, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<?> response = new Response<>(false, HttpStatus.BAD_REQUEST, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/doctorIdAndDateRange/{doctorId}")
    public ResponseEntity<Response<List<?>>> getAvailabilityByDoctorIdAndDateRange(@PathVariable String doctorId,@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        try {
        	List<AvailabilityDto> availableDoctors = appointmentService.getAvailabilityByDoctorIdAndDateRange(doctorId, startDate,endDate);
            Response<List<?>> response = new Response<>(true, HttpStatus.OK, availableDoctors, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
        	ex.printStackTrace();
            log.error("Exception: {}", ex.getMessage());
            Response<List<?>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/specializationAndDateRange/{specialization}")
    public ResponseEntity<Response<List<?>>> getAvailabilityBySpecializationAndDateRange(@PathVariable Specialization specialization,@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        try {
        	List<AvailabilityDto> availableDoctors = appointmentService.getAvailabilityBySpecializationAndDateRange(specialization, startDate,endDate);
            Response<List<?>> response = new Response<>(true, HttpStatus.OK, availableDoctors, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
        	ex.printStackTrace();
            log.error("Exception: {}", ex.getMessage());
            Response<List<?>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Gets available doctors.
     * 
     * @return ResponseEntity containing the list of available doctors
     */
//    @GetMapping("/availableDoctors")
//    public ResponseEntity<Response<List<?>>> getAvailableDoctors() {
//        try {
//            List<AvailabilityDto> availableDoctors = appointmentService.getAvailableDoctors();
//            Response<List<?>> response = new Response<>(true, HttpStatus.OK, availableDoctors, null);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (Exception ex) {
//            log.error("Exception: {}", ex.getMessage());
//            Response<List<?>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
//            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

   
    
    
  
    /**
    * Sets one availability
    * 
    * @param availability DTO
    * @return ResponseEntity indicating the status of the operation
    */
//  @PostMapping("/setDoctor")
//  public ResponseEntity<Response<?>> setAvailableDoctor(@RequestBody AvailabilityDto availabilityDto) {
//      try {
//          appointmentService.setAvailableDoctor(availabilityDto);
//          Response<?> response = new Response<>(true, HttpStatus.OK, null, null);
//          return new ResponseEntity<>(response, HttpStatus.OK);
//      } catch (Exception ex) {
//          log.error("Exception: {}", ex.getMessage());
//          Response<?> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
//          return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//      }
//  }

    /**
     * Shows availability of doctors.
     * 
     * @return ResponseEntity containing the list of doctor time slots
     */
//    @GetMapping("/showAvailability")
//    public ResponseEntity<Response<List<?>>> showAvailability() {
//        try {
//            List<String> doctorTimeSlots = appointmentService.showAvailability();
//            Response<List<?>> response = new Response<>(true, HttpStatus.OK, doctorTimeSlots, null);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (Exception ex) {
//            log.error("Exception: {}", ex.getMessage());
//            Response<List<?>> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
//            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * Notifies completion of an appointment.
     * 
     * @param appointmentId the ID of the appointment
     * @return ResponseEntity indicating the notification status
     */
    @PutMapping("/notifyCompletion/{appointmentId}")
    public ResponseEntity<Response<?>> notifyCompletion(@PathVariable String appointmentId) {
        try {
            appointmentService.notifyAfterCompletion(appointmentId);
            Response<?> response = new Response<>(true, HttpStatus.OK, null, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<?> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
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
    public ResponseEntity<Response<?>> handleAppointmentNotFoundException(AppointmentNotFoundException ex) {
        log.error("Exception: {}", ex.getMessage());
        Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles AvailabilityNotFoundException.
     * 
     * @param ex the exception
     * @return ResponseEntity containing the error message
     */
    @ExceptionHandler(AvailabilityNotFoundException.class)
    public ResponseEntity<Response<?>> handleAvailabilityNotFoundException(AvailabilityNotFoundException ex) {
        log.error("Exception: {}", ex.getMessage());
        Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}