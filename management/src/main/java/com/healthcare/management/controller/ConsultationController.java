package com.healthcare.management.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.management.dto.ConsultationDto;
import com.healthcare.management.entity.Appointment;
import com.healthcare.management.entity.Consultation;
//import com.healthcare.management.entity.Consultation;
import com.healthcare.management.service.ConsultationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/consultation")
public class ConsultationController {
	
	@Autowired
	private ConsultationService consultantService;
	
	
	@PostMapping("/create")
	public ConsultationDto addConsultation(@RequestBody @Valid ConsultationDto consultationDto){
		log.info("Creating a new consultation record..");
		Consultation consultation = consultantService.createConsultation(consultationDto);
        return new ConsultationDto(
            consultation.getConsultationId(),
            consultation.getAppointmentId(),
            consultation.getNotes(),
            consultation.getPrescription()
        );
	}
	
	@GetMapping
	public List<ConsultationDto> getConsultationDetails() {
		log.info("Loading all the consultation details...");
	    return consultantService.getAllConsultationDetails().stream()
	        .map(consultation -> {
	           
	            Integer appointmentId = consultation.getAppointmentId();
	            return new ConsultationDto(
	                consultation.getConsultationId(),
	                appointmentId,
	                consultation.getNotes(),
	                consultation.getPrescription()
	            );
	        })
	        .collect(Collectors.toList());
	}
	
	@GetMapping("/{appId}")
	public List<ConsultationDto>getConsultationDetailsByPatientID(@PathVariable int appId){
		log.info("Retreiving the consultation details filtered by Patient ID..");
		return consultantService.findConDetailsByAppId(appId).stream()
		            .map(consultation -> new ConsultationDto(
		                consultation.getConsultationId(),
		                consultation.getAppointmentId(),
		                consultation.getNotes(),
		                consultation.getPrescription()
		            ))
		            .collect(Collectors.toList());
	}
	
	@PutMapping("/update/{conId}")
	public ConsultationDto updateConsultation(@PathVariable int conId,@RequestBody @Valid ConsultationDto consultationDto) {
		log.info("Updating Consultation Details..");
		Consultation consultation = consultantService.updateConsultationDetailsById(conId, consultationDto);
        return new ConsultationDto(
            consultation.getConsultationId(),
            consultation.getAppointmentId(),
            consultation.getNotes(),
            consultation.getPrescription()
        );
	}
	
	@DeleteMapping("/delete/{conId}")
	public void removeConsultationDetails(@PathVariable int conId) {
		log.info("Deleting the consultation details..");
		consultantService.deleteConsultation(conId);
		log.info("Successfully removed the consultation entry..");
	}
	
}
