package com.healthcare.management.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.healthcare.management.dao.HistoryDAO;
import com.healthcare.management.dao.PatientDAO;
import com.healthcare.management.dto.HistoryDto;
import com.healthcare.management.entity.MedicalHistory;
import com.healthcare.management.entity.Patient;
import com.healthcare.management.exception.NoHistoryFoundException;
import com.healthcare.management.exception.NoPatientFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HistoryService {
	
	@Autowired
	private HistoryDAO historyDAO;
	
	@Autowired
	private PatientDAO patientDAO;

	/*public HistoryDto addHistory(HistoryDto historyDTO) {
	    log.info("Added a new record in medical history table..");
	    MedicalHistory medicalHistory = new MedicalHistory();
	    medicalHistory.setHistory_id(historyDTO.getHistoryId());
	    medicalHistory.setPatientId(historyDTO.getPatientId());
	    medicalHistory.setHealthHistory(historyDTO.getHealthHistory());

//	    Patient patient = patientDAO.findById(historyDTO.getPatientId())
//	            .orElseThrow(() -> new NoPatientFoundException("No Patient exists with the ID " + historyDTO.getPatientId()));
//	    medicalHistory.setPatientId(patient);

	    MedicalHistory savedMedicalHistory = historyDAO.save(medicalHistory);

	    HistoryDto historyDto = new HistoryDto();
	    historyDto.setHistoryId(savedMedicalHistory.getHistory_id());
	    historyDto.setPatientId(savedMedicalHistory.getPatientId());
	    historyDto.setHealthHistory(savedMedicalHistory.getHealthHistory());
	    return historyDto;
	}  */
	 
	public HistoryDto addHistory(HistoryDto historyDTO) {
	    log.info("Added a new record in medical history table..");
	    MedicalHistory medicalHistory = new MedicalHistory();
	    medicalHistory.setHistory_id(historyDTO.getHistoryId());
	    medicalHistory.setPatientId(historyDTO.getPatientId());
	    medicalHistory.setHealthHistory(historyDTO.getHealthHistory());

	    // Check if the patient exists
	    Patient patient = patientDAO.findById(historyDTO.getPatientId())
	            .orElseThrow(() -> new NoPatientFoundException("No Patient exists with the ID " + historyDTO.getPatientId()));
	    // medicalHistory.setPatientId(patient); // This line is not needed since patientId is a String

	    MedicalHistory savedMedicalHistory = historyDAO.save(medicalHistory);

	    HistoryDto historyDto = new HistoryDto();
	    historyDto.setHistoryId(savedMedicalHistory.getHistory_id());
	    historyDto.setPatientId(savedMedicalHistory.getPatientId());
	    historyDto.setHealthHistory(savedMedicalHistory.getHealthHistory());
	    return historyDto;
	}
	
	public HistoryDto getMedicalHistoryByHistoryId(Long historyId) {
        log.info("Retrieving medical history details of patient with history id " + historyId);
        MedicalHistory medicalHistory = historyDAO.findById(historyId)
                .orElseThrow(() -> new NoHistoryFoundException("No Medical History exists with the history Id " + historyId));
        
        HistoryDto historyDto = new HistoryDto();
        historyDto.setHistoryId(medicalHistory.getHistory_id());
        historyDto.setHealthHistory(medicalHistory.getHealthHistory());
        historyDto.setPatientId(medicalHistory.getPatientId());
        return historyDto;
    
    }
	
	public HistoryDto getHistoryByPatientId(String patientId) {
		log.info("Retrieving medical history details of patient with patient id " + patientId);
        MedicalHistory medicalHistory = historyDAO.getMedicalHistoryByPatientId(patientId);
        
        if (medicalHistory == null) {
            throw new NoHistoryFoundException("No Medical History exists for Patient ID: " + patientId);
        }
        
        HistoryDto historyDto = new HistoryDto();
        historyDto.setHistoryId(medicalHistory.getHistory_id());
        historyDto.setHealthHistory(medicalHistory.getHealthHistory());
        historyDto.setPatientId(medicalHistory.getPatientId());
        
        return historyDto;
    }
	
	
	
	public HistoryDto updateMedicalHistory(String patientId, HistoryDto medicalHistoryDTO) {
        MedicalHistory medicalHistory = historyDAO.getMedicalHistoryByPatientId(patientId);
        if (medicalHistory == null) {
            throw new NoHistoryFoundException("No medical history exists for Patient ID: " + patientId);
        }
        medicalHistory.setHealthHistory(medicalHistoryDTO.getHealthHistory());
        log.info("Successfully Updated the medical history of Patient with id " + patientId);
        MedicalHistory updatedMedicalHistory = historyDAO.save(medicalHistory);
        
        HistoryDto historyDto = new HistoryDto();
        historyDto.setHistoryId(updatedMedicalHistory.getHistory_id());
        historyDto.setHealthHistory(updatedMedicalHistory.getHealthHistory());
        historyDto.setPatientId(updatedMedicalHistory.getPatientId());
        
        return historyDto;
    }
	
	public void deleteMedicalHistory(String patientId) {
        MedicalHistory medicalHistory = historyDAO.getMedicalHistoryByPatientId(patientId);
        if (medicalHistory == null) {
            throw new NoHistoryFoundException("No medical history exists for Patient ID: " + patientId);
        }
        historyDAO.delete(medicalHistory);
        log.info("Successfully deleted the medical history of Patient with id " + patientId);
    }
	

}
