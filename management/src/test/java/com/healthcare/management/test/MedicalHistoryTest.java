package com.healthcare.management.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.healthcare.management.dao.HistoryDAO;
import com.healthcare.management.dao.PatientDAO;
import com.healthcare.management.dto.HistoryDto;
import com.healthcare.management.entity.MedicalHistory;
import com.healthcare.management.entity.Patient;
import com.healthcare.management.exception.NoHistoryFoundException;
import com.healthcare.management.exception.NoPatientFoundException;
import com.healthcare.management.service.HistoryService;
/**
 * MedicalHistoryTest is a test class for the HistoryService.
 * It uses Mockito to mock dependencies and JUnit 5 for testing.
 */
public class MedicalHistoryTest {

    @Mock
    private HistoryDAO historyDAO;

    @Mock
    private PatientDAO patientDAO;

    @InjectMocks
    private HistoryService historyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    /**
     * Tests the addHistory method.
     * Verifies that the method creates a new medical history record.
     */
    @Test
    public void testAddHistory() {
        HistoryDto historyDto = new HistoryDto();
        historyDto.setHistoryId("1");
        historyDto.setPatientId("2");
        historyDto.setHealthHistory("Test Health History");

        Patient patient = new Patient();
        when(patientDAO.findById("2")).thenReturn(Optional.of(patient));

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setHistoryId("1");
        medicalHistory.setPatientId("2");
        medicalHistory.setHealthHistory("Test Health History");

        when(historyDAO.save(any(MedicalHistory.class))).thenReturn(medicalHistory);

        HistoryDto result = historyService.addHistory(historyDto);

        assertNotNull(result);
        assertEquals(1L, result.getHistoryId());
        assertEquals("2", result.getPatientId());
        assertEquals("Test Health History", result.getHealthHistory());
        verify(patientDAO, times(1)).findById("2");
        verify(historyDAO, times(1)).save(any(MedicalHistory.class));
    }
    /**
     * Tests the addHistory method when no patient is found.
     * Verifies that the method throws NoPatientFoundException.
     */
    
    @Test
    public void testAddHistory_NoPatientFound() {
        HistoryDto historyDto = new HistoryDto();
        historyDto.setPatientId("1");

        when(patientDAO.findById("1")).thenReturn(Optional.empty());

        assertThrows(NoPatientFoundException.class, () -> {
            historyService.addHistory(historyDto);
        });
    }

    /**
     * Tests the getMedicalHistoryByHistoryId method.
     * Verifies that the method returns the correct medical history record.
     */
    @Test
    public void testGetMedicalHistoryByHistoryId() {
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setHistoryId("1");
        medicalHistory.setHealthHistory("Test Health History");
        //Patient patient = new Patient();
        medicalHistory.setPatientId("2");

        when(historyDAO.findById("1")).thenReturn(Optional.of(medicalHistory));

        HistoryDto result = historyService.getMedicalHistoryByHistoryId("1");

        assertNotNull(result);
        assertEquals(1L, result.getHistoryId());
        assertEquals("Test Health History", result.getHealthHistory());
        assertEquals("2", result.getPatientId());
        verify(historyDAO, times(1)).findById("1");
    }

    /**
     * Tests the getMedicalHistoryByHistoryId method when no medical history is found.
     * Verifies that the method throws NoHistoryFoundException.
     */
    @Test
    public void testGetMedicalHistoryByHistoryId_NotFound() {
        when(historyDAO.findById("1")).thenReturn(Optional.empty());

        assertThrows(NoHistoryFoundException.class, () -> {
            historyService.getMedicalHistoryByHistoryId("1");
        });
    }
    
    /**
     * Tests the getHistoryByPatientId method.
     * Verifies that the method returns the correct medical history record.
     */
    @Test
    public void testGetHistoryByPatientId() {
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setHistoryId("1");
        medicalHistory.setHealthHistory("Test Health History");
       // Patient patient = new Patient();
        medicalHistory.setPatientId("2");

        when(historyDAO.getMedicalHistoryByPatientId("patient1")).thenReturn(medicalHistory);

        HistoryDto result = historyService.getHistoryByPatientId("patient1");

        assertNotNull(result);
        assertEquals(1L, result.getHistoryId());
        assertEquals("Test Health History", result.getHealthHistory());
        verify(historyDAO, times(1)).getMedicalHistoryByPatientId("patient1");
    }
    
    /**
     * Tests the getHistoryByPatientId method when no medical history is found.
     * Verifies that the method throws NoHistoryFoundException.
     */

    @Test
    public void testGetHistoryByPatientId_NotFound() {
        when(historyDAO.getMedicalHistoryByPatientId("patient1")).thenReturn(null);

        assertThrows(NoHistoryFoundException.class, () -> {
            historyService.getHistoryByPatientId("patient1");
        });
    }

    
    /**
     * Tests the updateMedicalHistory method.
     * Verifies that the method updates the medical history record.
     */
    @Test
    public void testUpdateMedicalHistory() {
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setHistoryId("1");
        medicalHistory.setHealthHistory("Old Health History");
       // Patient patient = new Patient();
        medicalHistory.setPatientId("2");

        HistoryDto historyDto = new HistoryDto();
        historyDto.setHealthHistory("Updated Health History");

        when(historyDAO.getMedicalHistoryByPatientId("patient1")).thenReturn(medicalHistory);
        when(historyDAO.save(any(MedicalHistory.class))).thenReturn(medicalHistory);

        HistoryDto result = historyService.updateMedicalHistory("patient1", historyDto);

        assertNotNull(result);
        assertEquals("Updated Health History", result.getHealthHistory());
        verify(historyDAO, times(1)).getMedicalHistoryByPatientId("patient1");
        verify(historyDAO, times(1)).save(any(MedicalHistory.class));
    }
    /**
     * Tests the updateMedicalHistory method when no medical history is found.
     * Verifies that the method throws NoHistoryFoundException.
     */
    @Test
    public void testUpdateMedicalHistory_NotFound() {
        HistoryDto historyDto = new HistoryDto();
        historyDto.setHealthHistory("Updated Health History");

        when(historyDAO.getMedicalHistoryByPatientId("patient1")).thenReturn(null);

        assertThrows(NoHistoryFoundException.class, () -> {
            historyService.updateMedicalHistory("patient1", historyDto);
        });
    }
    
    /**
     * Tests the deleteMedicalHistory method.
     * Verifies that the method deletes the medical history record.
     */

    @Test
    public void testDeleteMedicalHistory() {
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setHistoryId("1");
       // Patient patient = new Patient();
        medicalHistory.setPatientId("2");

        when(historyDAO.getMedicalHistoryByPatientId("patient1")).thenReturn(medicalHistory);

        historyService.deleteMedicalHistory("patient1");

        verify(historyDAO, times(1)).getMedicalHistoryByPatientId("patient1");
        verify(historyDAO, times(1)).delete(medicalHistory);
    }

    
    /**
     * Tests the deleteMedicalHistory method when no medical history is found.
     * Verifies that the method throws NoHistoryFoundException.
     */
    @Test
    public void testDeleteMedicalHistory_NotFound() {
        when(historyDAO.getMedicalHistoryByPatientId("patient1")).thenReturn(null);

        assertThrows(NoHistoryFoundException.class, () -> {
            historyService.deleteMedicalHistory("patient1");
        });
    }
}