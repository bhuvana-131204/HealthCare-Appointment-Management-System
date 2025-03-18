package com.dataBase.automate.service;


import com.dataBase.automate.dto.AppointmentDto;
import com.dataBase.automate.dto.AvailabilityDto;
import com.dataBase.automate.exception.AppointmentNotFoundException;
import com.dataBase.automate.exception.AvailabilityConflictException;
import com.dataBase.automate.exception.AvailabilityNotFoundException;
import com.dataBase.automate.model.Appointment;
import com.dataBase.automate.model.Availability;
import com.dataBase.automate.model.Specialization;
import com.dataBase.automate.model.Status;
import com.dataBase.automate.model.TimeSlots;
import com.dataBase.automate.repository.AppointmentRepository;
import com.dataBase.automate.repository.AvailabilityRepository;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing appointments.
 * 
 * @Author Sanjay R
 * @Since 2025-03-18
 */
@Slf4j
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    /**
     * Creates a new appointment.
     * 
     * @param availabilityId the ID of the availability
     * @return the created appointment
     */
    public Appointment createAppointment(String availabilityId) {
        if (availabilityId == null) {
            throw new IllegalArgumentException("Availability ID cannot be null");
        }

        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new AvailabilityNotFoundException("Availability not found for ID: " + availabilityId));

        Appointment appointment = new Appointment();
        appointment.setTimeSlot(availability.getSlots());
        appointment.setStatus(Status.Pending);
        appointment.setDoctorId(availability.getDoctorId());
        appointment.setPatientId("1"); // Manual Set
        appointment.setDate(availability.getDate());
        appointment.setAvailabilityId(availabilityId);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        notifyAfterBooking(appointment);
        return savedAppointment;
    }

    /**
     * Views all appointments.
     * 
     * @return the list of appointments
     */
    public List<Appointment> viewAppointments() {
        return appointmentRepository.findAll();
    }

    /**
     * Fetches an appointment by ID.
     * 
     * @param id the ID of the appointment
     * @return the appointment
     */
    public Optional<Appointment> fetchAppointmentById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            log.info("Exiting fetchAppointmentById method");
            return optionalAppointment;
        } else {
            log.error("Appointment not found for ID: {}", id);
            throw new AppointmentNotFoundException("Appointment not found for ID: " + id);
        }
    }

    /**
     * Updates an appointment by ID and new availability ID.
     * 
     * @param id the ID of the appointment
     * @param newAvailabilityId the new availability ID
     * @return the updated appointment
     */
    public Appointment updateAppointment(String id, String newAvailabilityId) {
        if (id == null || newAvailabilityId == null) {
            throw new IllegalArgumentException("Appointment ID and New Availability ID cannot be null");
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found for ID: " + id));

        Optional<Appointment> conflictingAppointment = appointmentRepository.findByAvailabilityId(newAvailabilityId);
        if (conflictingAppointment.isPresent() && !conflictingAppointment.get().getAppointmentId().equals(id)) {
            throw new AvailabilityConflictException("Pick another availability");
        }

        Availability availability = availabilityRepository.findById(newAvailabilityId)
                .orElseThrow(() -> new AvailabilityNotFoundException("Availability not found for ID: " + newAvailabilityId));

        appointment.setAvailabilityId(newAvailabilityId);
        appointment.setTimeSlot(availability.getSlots());
        appointment.setDate(availability.getDate());
        appointment.setDoctorId(availability.getDoctorId());
        notifyAfterUpdate(appointment.getAvailabilityId(), newAvailabilityId);

        return appointmentRepository.save(appointment);
    }

    /**
     * Cancels an appointment by ID.
     * 
     * @param id the ID of the appointment
     */
    public void cancelAppointment(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found for ID: " + id));
        appointment.setStatus(Status.Cancelled);
        appointmentRepository.save(appointment);
    }

    /**
     * Fetches appointments by patient ID.
     * 
     * @param patientId the ID of the patient
     * @return the list of appointments
     */
    public List<Appointment> fetchAppointmentsByPatientId(String patientId) {
        try {
            return appointmentRepository.findByPatientId(patientId);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            throw ex;
        }
    }

    /**
     * Fetches appointments by doctor ID.
     * 
     * @param doctorId the ID of the doctor
     * @return the list of appointments
     */
    public List<Appointment> fetchAppointmentsByDoctorId(String doctorId) {
        try {
            return appointmentRepository.findByDoctorId(doctorId);
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            throw ex;
        }
    }

    /**
     * Notifies the doctor for removal of an appointment.
     * 
     * @param doctorId the ID of the doctor
     * @param patientId the ID of the patient
     * @param id the ID of the appointment
     */
    public void notifyDoctorForRemoval(String doctorId, String patientId, String id) {
        if (doctorId == null || patientId == null || id == null) {
            throw new IllegalArgumentException("Doctor ID or Patient ID and Appointment ID cannot be null");
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found for ID: " + id));
        appointment.setStatus(Status.Cancelled);
        appointmentRepository.save(appointment);
        log.info("Doctor {} notified for schedule cancellation for patient {}", doctorId, patientId);
    }

    /**
     * Notifies after booking an appointment.
     * 
     * @param appointment the appointment
     */
    public void notifyAfterBooking(Appointment appointment) {
        if (appointment == null) {
            throw new AppointmentNotFoundException("Appointment not found");
        }

        // logic for Notification module
    }

    /**
     * Notifies after updating an appointment.
     * 
     * @param availabilityId the old availability ID
     * @param newAvailabilityId the new availability ID
     */
    public void notifyAfterUpdate(String availabilityId, String newAvailabilityId) {
        if (availabilityId == null || newAvailabilityId == null) {
            throw new IllegalArgumentException("Availability ID and New Availability ID cannot be null");
        }

        // logic for Notification module
    }

    /**
     * Deletes an appointment by ID.
     * 
     * @param appointmentId the ID of the appointment
     */
    public void deleteAppointment(String appointmentId) {
        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }

        if (appointmentRepository.existsById(appointmentId)) {
            appointmentRepository.deleteById(appointmentId);
        } else {
            throw new AppointmentNotFoundException("Appointment not found for ID: " + appointmentId);
        }
    }

    /**
     * Notifies after completing an appointment.
     * 
     * @param appointmentId the ID of the appointment
     */
    public void notifyAfterCompletion(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found for ID: " + appointmentId));
        appointment.setStatus(Status.Completed);
        // logic for Consultation module
    }

    // Availability methods

    /**
     * Gets all available doctors.
     * 
     * @return the list of available doctors
     */
    public List<AvailabilityDto> getAvailableDoctors() {
        return availabilityRepository.findAll().stream()
                .map(availability -> new AvailabilityDto(availability.getAvailabilityId(), availability.getSlots(),
                        availability.getDate(), availability.getDoctorId(), availability.getSpecialization()))
                .collect(Collectors.toList());
    }

   
    /**
     * Sets a single available doctor.
     * 
     * @param availabilityDto the availability DTO
     */
    public void setAvailableDoctor(AvailabilityDto availabilityDto) {
        if (availabilityDto == null) {
            throw new IllegalArgumentException("Availability DTO cannot be null");
        }

        Availability availability = new Availability();
        availability.setSlots(availabilityDto.getSlots());
        availability.setDate(availabilityDto.getDate());
        availability.setDoctorId(availabilityDto.getDoctorId());
        availability.setSpecialization(availabilityDto.getSpecialization());
        availabilityRepository.save(availability);
    }
    /**
     * Shows availability of doctors.
     * 
     * @return the list of doctor time slots
     */
    public List<String> showAvailability() {
        List<Availability> availabilities = availabilityRepository.findAll();
        Map<String, List<String>> doctorTimeSlotsMap = new HashMap<>();

        for (Availability availability : availabilities) {
            String doctorId = availability.getDoctorId();
            String timeSlot = availability.getSlots().getDisplayName();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(availability.getDate());

            String timeSlotWithDate = date + " : " + timeSlot;

            doctorTimeSlotsMap.computeIfAbsent(doctorId, k -> new ArrayList<>()).add(timeSlotWithDate);
        }

        return doctorTimeSlotsMap.entrySet().stream()
                .map(entry -> entry.getKey() + " : " + String.join(", ", entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Gets available doctors by date and specialization.
     * 
     * @param date the date
     * @param specialization the specialization
     * @return the list of available doctors
     */
    public List<String> getAvailableDoctorsByDateAndSpecialization(String date, String specialization) {
        if (date == null || specialization == null) {
            throw new IllegalArgumentException("Date and Specialization cannot be null");
        }

        // logic for availability controller
        return new ArrayList<>();
    }

    /**
     * Gets available doctors by date and doctor ID.
     * 
     * @param date the date
     * @param doctorId the ID of the doctor
     * @return the list of available doctors
     */
    public List<String> getAvailableDoctorsByDateAndId(String date, String doctorId) {
        if (date == null || doctorId == null) {
            throw new IllegalArgumentException("Date and Doctor ID cannot be null");
        }

        // logic for availability controller
        return new ArrayList<>();
    }

    /**
     * Updates appointment after notification.
     * 
     * @param appointmentId the ID of the appointment
     */
    public void updationAfterNotification(String appointmentId) {
        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus(Status.Cancelled);
            appointmentRepository.save(appointment);
        } else {
            throw new AppointmentNotFoundException("Appointment for this availability is not found");
        }
    }
}