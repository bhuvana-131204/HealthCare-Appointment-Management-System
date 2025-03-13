package com.dataBase.automate.service;

import com.dataBase.automate.controller.AppointmentController;
import com.dataBase.automate.dto.AppointmentDto;
import com.dataBase.automate.dto.AvailabilityDto;
import com.dataBase.automate.exception.AppointmentNotFoundException;
import com.dataBase.automate.exception.AvailabilityNotFoundException;
import com.dataBase.automate.model.Appointment;
import com.dataBase.automate.model.Availability;
import com.dataBase.automate.model.Specialization;
import com.dataBase.automate.model.Status;
import com.dataBase.automate.model.TimeSlots;
import com.dataBase.automate.repository.AppointmentRepository;
import com.dataBase.automate.repository.AvailabilityRepository;

import jakarta.transaction.Transactional;
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

@Slf4j
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public Appointment createAppointment( String availabilityId) {

        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new AvailabilityNotFoundException("Availability not found for ID: " + availabilityId));

        Appointment appointment = new Appointment();
        appointment.setTimeSlot(availability.getSlots());
        appointment.setStatus(Status.Pending);
        appointment.setDoctorId(availability.getDoctorId());
        appointment.setPatientId("1"); // Manual Set
        appointment.setDate(availability.getDate());
       
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return savedAppointment;
    }

    public List<Appointment> viewAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments;
    }

    public Optional<Appointment> fetchAppointmentById(String id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            log.info("Exiting fetchAppointmentById method");
            return optionalAppointment;
        } else {
            log.error("Appointment not found for ID: {}", id);
            throw new AppointmentNotFoundException("Appointment not found for ID: " + id);
        }
    }
    public void notifyDoctorForUpdatedSchedule(String doctorId, String patientId,String id) {
        log.info("Notifying doctor {} for updated schedule for patient {}", doctorId, patientId);
        // Logic to notify doctor about updated schedule
        // For example, sending an email or message to the doctor
        // Update the appointment status to Pending
        Appointment appointment = appointmentRepository.findById(id).get();
       
            appointment.setStatus(Status.Pending);
            appointmentRepository.save(appointment);
        
        log.info("Doctor {} notified for updated schedule for patient {}", doctorId, patientId);
    }

    public void notifyDoctorForRemoval(String doctorId, String patientId,String id) {
        log.info("Notifying doctor {} for schedule cancellation for patient {}", doctorId, patientId);
        // Logic to notify doctor about appointment removal
        // For example, sending an email or message to the doctor
        // Update the appointment status to Cancelled
        Appointment appointment = appointmentRepository.findById(id).get();
        
            appointment.setStatus(Status.Cancelled);
            appointmentRepository.save(appointment);
        
        log.info("Doctor {} notified for schedule cancellation for patient {}", doctorId, patientId);
    }

    public Appointment updateAppointment(String id, AppointmentDto appointmentDto) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setTimeSlot(appointmentDto.getTimeSlot());
            appointment.setStatus(appointmentDto.getStatus());
            Appointment updatedAppointment = appointmentRepository.save(appointment);
            return updatedAppointment;
        } else {
            log.error("Appointment not found for ID: {}", id);
            throw new AppointmentNotFoundException("Appointment not found for ID: " + id);
        }
    }

    public void cancelAppointment(String id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        } else {
            log.error("Appointment not found for ID: {}", id);
            throw new AppointmentNotFoundException("Appointment not found for ID: " + id);
        }
    }

    public void notifyAfterBooking(String appointmentId)//works 
    {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus(Status.Booked);
            appointmentRepository.save(appointment);
        } else {
            log.error("Appointment not found for ID: {}", appointmentId);
            throw new AppointmentNotFoundException("Appointment not found for ID: " + appointmentId);
        }
    }

    public void notifyAfterUpdate(String appointmentId, TimeSlots newTimeSlot) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setTimeSlot(newTimeSlot);
            appointment.setStatus(Status.Pending); // Set status to Pending
            appointmentRepository.save(appointment);
        } else {
            log.error("Appointment not found for ID: {}", appointmentId);
            throw new AppointmentNotFoundException("Appointment not found for ID: " + appointmentId);
        }
    }

    public void notifyAfterCancellation(String appointmentId) {
        if (appointmentRepository.existsById(appointmentId)) {
            appointmentRepository.deleteById(appointmentId);
        } else {
            log.error("Appointment not found for ID: {}", appointmentId);
            throw new AppointmentNotFoundException("Appointment not found for ID: " + appointmentId);
        }
    }

    public List<AvailabilityDto> getAvailableDoctors() {
        List<Availability> availabilities = availabilityRepository.findAll();
        List<AvailabilityDto> availableDoctors = availabilities.stream()
                .map(availability -> new AvailabilityDto(
                        availability.getAvailabilityId(),
                        availability.getSlots(),
                        availability.getDate(),
                        availability.getDoctorId(),
                        availability.getSpecialization()))
                .collect(Collectors.toList());
        return availableDoctors;
    }

    public void setAvailableDoctors(List<AvailabilityDto> availabilityDtos) {
        for (AvailabilityDto dto : availabilityDtos) {
            setAvailableDoctor(dto);
        }
    }

    public void setAvailableDoctor(AvailabilityDto availabilityDto) {
        Availability availability = new Availability();
        availability.setSlots(availabilityDto.getSlots());
        availability.setDate(availabilityDto.getDate());
        availability.setDoctorId(availabilityDto.getDoctorId());
        availability.setSpecialization(availabilityDto.getSpecialization());
        availabilityRepository.save(availability);
    }

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

        List<String> doctorTimeSlots = doctorTimeSlotsMap.entrySet().stream()
                .map(entry -> entry.getKey() + " : " + String.join(", ", entry.getValue()))
                .collect(Collectors.toList());

        return doctorTimeSlots;
    }
    public List<Availability> getAllAvailability() {
        List<Availability> availabilities = availabilityRepository.findAll();
        return availabilities;
    }

    public Appointment doctorAccepted(String appointmentId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus(Status.Booked);
            Appointment updatedAppointment = appointmentRepository.save(appointment);
            return updatedAppointment;
        } else {
            log.error("Appointment not found for ID: {}", appointmentId);
            throw new AppointmentNotFoundException("Appointment not found for ID: " + appointmentId);
        }
    
    }
    public List<String> getAvailableDoctorsByDate(String date, Specialization specialization) {
        Date utilDate = parseDate(date);
        List<Availability> availabilities = availabilityRepository.findByDateAndSpecialization(utilDate, specialization);
        List<String> availableDoctors = availabilities.stream()
                .map(availability -> availability.getDoctorId() + " : " + availability.getSlots().getDisplayName())
                .collect(Collectors.toList());
        return availableDoctors;
    }

    public List<String> getAvailableDoctorsByDateAndSlot(String date, TimeSlots slot, Specialization specialization) {
        Date utilDate = parseDate(date);
        List<Availability> availabilities = availabilityRepository.findByDateAndSlotsAndSpecialization(utilDate, slot, specialization);
        List<String> availableDoctors = availabilities.stream()
                .map(Availability::getDoctorId)
                .distinct()
                .collect(Collectors.toList());
        return availableDoctors;
    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

}