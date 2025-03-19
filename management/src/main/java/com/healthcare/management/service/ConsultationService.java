package com.healthcare.management.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.management.dao.AppointmentDAO;
import com.healthcare.management.dao.ConsultationDAO;
import com.healthcare.management.dto.ConsultationDto;
import com.healthcare.management.entity.Appointment;
import com.healthcare.management.entity.Consultation;
import com.healthcare.management.exception.ConsultationAlreadyExistsException;
import com.healthcare.management.exception.NoAppointmentFoundException;
import com.healthcare.management.exception.NoConsultationDetailsFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsultationService {
	
	@Autowired
	private ConsultationDAO consultationDAO;
	
	@Autowired
	private AppointmentDAO appointmentDAO;
	/**
	 * Retrieves all consultation records.
	 * 
	 * @return List<Consultation> - A list of all consultation records.
	 */
	public List<Consultation> getAllConsultationDetails() {
		log.info("Retrieving the consultation details...");
		return consultationDAO.findAll();
	}
	
	/**
	 * Retrieves consultation details by consultation ID.
	 * 
	 * @param consultationId - The ID of the consultation to be retrieved.
	 * @return Consultation - The consultation record.
	 * @throws NoConsultationDetailsFoundException - If no consultation details are found for the given ID.
	 */
	public Consultation getConsultationDetailsById(String consultationId) {
		log.info("Retrieving the consultation details filtered by consultation id...");
		 
		return consultationDAO.findByConsultationId(consultationId).orElseThrow(()-> 
				new NoConsultationDetailsFoundException("No Consultation Details found at ID"+consultationId));
		
	}
	
	/**
	 * Creates a new consultation record.
	 * 
	 * @param consultationDto - The consultation data transfer object containing the details of the consultation to be created.
	 * @return Consultation - The created consultation record.
	 * @throws NoAppointmentFoundException - If no appointment is found for the given appointment ID.
	 * @throws ConsultationAlreadyExistsException - If a consultation already exists for the given appointment ID.
	 */
	
	 public Consultation createConsultation(ConsultationDto consultationDto) {
	        log.info("Creating new Consultation Record...");
	        log.info("Received ConsultationDto: {}", consultationDto);

	        Appointment appointment = appointmentDAO.findById(consultationDto.getAppointmentId())
	                .orElseThrow(() -> new NoAppointmentFoundException("No Appointment found at ID " + consultationDto.getAppointmentId()));

	        List<Consultation> existingConsultation = consultationDAO.findConsultationDetailsByAppointmentId(consultationDto.getAppointmentId());
	        if (!existingConsultation.isEmpty()) {
	            throw new ConsultationAlreadyExistsException("A consultation already exists for appointment ID " + consultationDto.getAppointmentId());
	        }

	        Consultation consultation = new Consultation();
	        consultation.setAppointmentId(consultationDto.getAppointmentId());
	        consultation.setNotes(consultationDto.getNotes());
	        consultation.setPrescription(consultationDto.getPrescription());

	        return consultationDAO.save(consultation);
	    }
	
	
	 /**
		 * Retrieves consultation details filtered by appointment ID.
		 * 
		 * @param appointmentId - The appointment ID to filter consultation records.
		 * @return List<Consultation> - A list of consultation records filtered by appointment ID.
		 *//**
		 * Retrieves consultation details filtered by appointment ID.
		 * 
		 * @param appointmentId - The appointment ID to filter consultation records.
		 * @return List<Consultation> - A list of consultation records filtered by appointment ID.
		 */
	
	public List<Consultation>findConDetailsByAppId(String appointmentId){
		log.info("Retrieving consultation details filtered by appointment ID...");
		return consultationDAO.findConsultationDetailsByAppointmentId(appointmentId);
	}
	
//	public Consultation updateConsultationDetailsById(int consultationId,Consultation consultationDetails) {
//		Consultation consultation = consultationDAO.findById(consultationId).orElseThrow(()->
//			new NoConsultationDetailsFoundException("No Consultation Details found at ID"+consultationId));
//		
//		consultation.setNotes(consultationDetails.getNotes());
//		consultation.setPrescription(consultationDetails.getPrescription());
//		
//		return consultationDAO.save(consultation);
//	}
	/**
	 * Updates an existing consultation record by consultation ID.
	 * 
	 * @param consultationId - The ID of the consultation to be updated.
	 * @param consultationDetails - The consultation data transfer object containing the updated details of the consultation.
	 * @return Consultation - The updated consultation record.
	 * @throws NoConsultationDetailsFoundException - If no consultation details are found for the given ID.
	 */
	public Consultation updateConsultationDetailsById(String consultationId, ConsultationDto consultationDetails) {
        Consultation consultation = consultationDAO.findById(consultationId).orElseThrow(() -> 
            new NoConsultationDetailsFoundException("No Consultation Details found at ID " + consultationId));

        consultation.setNotes(consultationDetails.getNotes());
        consultation.setPrescription(consultationDetails.getPrescription());
        log.info("Successfully updated the consultation details..");
        return consultationDAO.save(consultation);
    }
	
	/**
	 * Deletes a consultation record by consultation ID.
	 * 
	 * @param consultationId - The ID of the consultation to be deleted.
	 * @throws NoConsultationDetailsFoundException - If no consultation details are found for the given ID.
	 */
	
	public void deleteConsultation(String consultationId) {
		Consultation consultation = getConsultationDetailsById(consultationId);
		consultationDAO.delete(consultation);
		log.info("Successfully removed the consultation entry");
	}
}