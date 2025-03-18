package com.healthcare.management.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.healthcare.management.dao.AppointmentDAO;
import com.healthcare.management.dao.ConsultationDAO;
import com.healthcare.management.dto.ConsultationDto;
import com.healthcare.management.entity.Appointment;
import com.healthcare.management.entity.Consultation;
import com.healthcare.management.exception.NoAppointmentFoundException;
import com.healthcare.management.exception.NoConsultationDetailsFoundException;
import com.healthcare.management.service.ConsultationService;

@ExtendWith(MockitoExtension.class)
public class ConsultationServiceTest {
	
	@Mock
	private ConsultationDAO consultationDAO;
	
	@Mock
	private AppointmentDAO appointmentDAO;
	
	@InjectMocks
	private ConsultationService consultationService;
	
	@BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void testGetAllConsultationDetails() {
		List<Consultation> consultations = Arrays.asList(new Consultation(),new Consultation());
		when(consultationDAO.findAll()).thenReturn(consultations);
		List<Consultation> result = consultationService.getAllConsultationDetails();
		assertEquals(2, result.size());
		verify(consultationDAO,times(1)).findAll();
		
		
	}
	
	
	@Test
	public void testGetConsultationDetailsById() {
		Consultation consultation = new Consultation();
		when(consultationDAO.findByConsultationId(1)).thenReturn(Optional.of(consultation));
		Consultation result = consultationService.getConsultationDetailsById(1);
		assertNotNull(result);
		verify(consultationDAO,times(1)).findByConsultationId(1);
	}
	
	 @Test
	    public void testGetConsultationDetailsById_NotFound() {
	        when(consultationDAO.findByConsultationId(1)).thenReturn(Optional.empty());

	        assertThrows(NoConsultationDetailsFoundException.class, () -> {
	            consultationService.getConsultationDetailsById(1);
	        });
	    }
	 
	 @Test
	    public void testCreateConsultation() {
	        ConsultationDto consultationDto = new ConsultationDto();
	        consultationDto.setAppointmentId(1);
	        consultationDto.setNotes("Test Notes");
	        consultationDto.setPrescription("Test Prescription");

	        Appointment appointment = new Appointment();
	        when(appointmentDAO.findById(1)).thenReturn(Optional.of(appointment));

	        Consultation consultation = new Consultation();
	        consultation.setAppointment(appointment);
	        consultation.setNotes("Test Notes");
	        consultation.setPrescription("Test Prescription");

	        when(consultationDAO.save(any(Consultation.class))).thenReturn(consultation);

	        Consultation result = consultationService.createConsultation(consultationDto);

	        assertNotNull(result);
	        assertEquals("Test Notes", result.getNotes());
	        assertEquals("Test Prescription", result.getPrescription());
	        verify(appointmentDAO, times(1)).findById(1);
	        verify(consultationDAO, times(1)).save(any(Consultation.class));
	    }
	 
	 
	 @Test
	    public void testCreateConsultation_NoAppointmentFound() {
	        ConsultationDto consultationDto = new ConsultationDto();
	        consultationDto.setAppointmentId(1);

	        when(appointmentDAO.findById(1)).thenReturn(Optional.empty());

	        assertThrows(NoAppointmentFoundException.class, () -> {
	            consultationService.createConsultation(consultationDto);
	        });
	    }
	 
	 @Test
	    public void testUpdateConsultationDetailsById() {
	        ConsultationDto consultationDto = new ConsultationDto();
	        consultationDto.setNotes("Updated Notes");
	        consultationDto.setPrescription("Updated Prescription");

	        Consultation consultation = new Consultation();
	        when(consultationDAO.findById(1)).thenReturn(Optional.of(consultation));

	        when(consultationDAO.save(any(Consultation.class))).thenReturn(consultation);

	        Consultation result = consultationService.updateConsultationDetailsById(1, consultationDto);

	        assertNotNull(result);
	        assertEquals("Updated Notes", result.getNotes());
	        assertEquals("Updated Prescription", result.getPrescription());
	        verify(consultationDAO, times(1)).findById(1);
	        verify(consultationDAO, times(1)).save(any(Consultation.class));
	    }

	    @Test
	    public void testUpdateConsultationDetailsById_NotFound() {
	        ConsultationDto consultationDto = new ConsultationDto();
	        consultationDto.setNotes("Updated Notes");
	        consultationDto.setPrescription("Updated Prescription");

	        when(consultationDAO.findById(1)).thenReturn(Optional.empty());

	        assertThrows(NoConsultationDetailsFoundException.class, () -> {
	            consultationService.updateConsultationDetailsById(1, consultationDto);
	        });
	    }

	    @Test
	    public void testDeleteConsultation() {
	        Consultation consultation = new Consultation();
	        when(consultationDAO.findByConsultationId(1)).thenReturn(Optional.of(consultation));

	        consultationService.deleteConsultation(1);

	        verify(consultationDAO, times(1)).findByConsultationId(1);
	        verify(consultationDAO, times(1)).delete(consultation);
	    }

	    @Test
	    public void testDeleteConsultation_NotFound() {
	        when(consultationDAO.findByConsultationId(1)).thenReturn(Optional.empty());

	        assertThrows(NoConsultationDetailsFoundException.class, () -> {
	            consultationService.deleteConsultation(1);
	        });
	    }
}
