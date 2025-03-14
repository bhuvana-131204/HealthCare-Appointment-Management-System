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

@Slf4j
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // 1. CREATING AN APPOINTMENT
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

    // 2. VIEW ALL APPOINTMENTS
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

    // 3. VIEW APPOINTMENT BY APPOINTMENT ID
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

    // 4. UPDATE APPOINTMENT BY BODY
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

    // 5. CANCEL APPOINTMENT
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

    
    

    // 13. AVAILABLE DOCTORS BY DATE AND SPECIALIZATION
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
 // 14. AVAILABLE DOCTORS BY DATE, TIMESLOT AND SPECIALIZATION
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

    // 15. ACCEPTED NOTIFICATION
    @PutMapping("/acceptedNotification/{availabilityId}{type}")
    public ResponseEntity<Response<Void>> AppointmentNotification(@PathVariable String availabilityId,@PathVariable String type) {
        try {
        	
            appointmentService.updationAfterNotification(availabilityId, type) ;
                
        	
        	
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

    // 16. REJECTED NOTIFICATION
   
    
    
//AVAILABILITY SECTION
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

    // 7. ENTER LIST OF AVAILABILITY
    @PostMapping("/setAvailableDoctors")
    public ResponseEntity<Response<Void>> setAvailableDoctors(@RequestBody List<AvailabilityDto> availabilityDtos) {
        try {
            appointmentService.setAvailableDoctors(availabilityDtos);
            Response<Void> response = new Response<>(true, HttpStatus.OK, null, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            Response<Void> response = new Response<>(false, HttpStatus.INTERNAL_SERVER_ERROR, null, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 8. ENTER ONE AVAILABILITY
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
    // Exception Handlers
    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<Response<String>> handleAppointmentNotFoundException(AppointmentNotFoundException ex) {
        log.error("Exception: {}", ex.getMessage());
        Response<String> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AvailabilityNotFoundException.class)
    public ResponseEntity<Response<String>> handleAvailabilityNotFoundException(AvailabilityNotFoundException ex) {
        log.error("Exception: {}", ex.getMessage());
        Response<String> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}