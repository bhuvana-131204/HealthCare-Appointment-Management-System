package com.healthcare.management.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.management.dao.AppointmentDAO;
import com.healthcare.management.dao.ConsultationDAO;
import com.healthcare.management.dto.ConsultationDto;
import com.healthcare.management.entity.Appointment;
import com.healthcare.management.entity.Consultation;
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
	
	public List<Consultation> getAllConsultationDetails() {
		log.info("Retrieving the consultation details...");
		return consultationDAO.findAll();
	}
	
	public Consultation getConsultationDetailsById(int consultationId) {
		log.info("Retrieving the consultation details filtered by consultation id...");
		 
		return consultationDAO.findByConsultationId(consultationId).orElseThrow(()-> 
				new NoConsultationDetailsFoundException("No Consultation Details found at ID"+consultationId));
		
	}
	
	public Consultation createConsultation(ConsultationDto consultationDto) {
	    log.info("Creating new Consultation Record...");
	    log.info("Received ConsultationDto: {}", consultationDto);
	    
	    Appointment appointment = appointmentDAO.findById(consultationDto.getAppointmentId())
	            .orElseThrow(() -> new NoAppointmentFoundException("No Appointment found at ID " + consultationDto.getAppointmentId()));
	    Consultation consultation = new Consultation();
	    consultation.setAppointmentId(consultation.getAppointmentId());
	    consultation.setNotes(consultationDto.getNotes());
	    consultation.setPrescription(consultationDto.getPrescription());
	    return consultationDAO.save(consultation);
	}
	
	
	/*public ConsultationDto createConsultation(ConsultationDto consultationDto) {
        log.info("Creating new Consultation Record...");
        log.info("Received ConsultationDto: {}", consultationDto);
        
        Appointment appointment = appointmentDAO.findById(consultationDto.getAppointmentId())
                .orElseThrow(() -> new NoAppointmentFoundException("No Appointment found at ID " + consultationDto.getAppointmentId()));
        Consultation consultation = new Consultation();
        consultation.setAppointment(appointment);
        consultation.setNotes(consultationDto.getNotes());
        consultation.setPrescription(consultationDto.getPrescription());
        Consultation savedConsultation = consultationDAO.save(consultation);
        
        return new ConsultationDto(
            savedConsultation.getConsultationId(),
            savedConsultation.getAppointment().getAppointment_id(),
            savedConsultation.getNotes(),
            savedConsultation.getPrescription()
        );
    }*/
	
	public List<Consultation>findConDetailsByAppId(int appointmentId){
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
	
	public Consultation updateConsultationDetailsById(int consultationId,ConsultationDto consultationDetails) {
		Consultation consultation = consultationDAO.findById(consultationId).orElseThrow(()->
			new NoConsultationDetailsFoundException("No Consultation Details found at ID"+consultationId));
		
		consultation.setNotes(consultationDetails.getNotes());
		consultation.setPrescription(consultationDetails.getPrescription());
		log.info("Successfully updated the consultation details..");
		return consultationDAO.save(consultation);
	}
	
	public void deleteConsultation(int consultationId) {
		Consultation consultation = getConsultationDetailsById(consultationId);
		consultationDAO.delete(consultation);
		log.info("Successfully removed the consultation entry");
	}
}