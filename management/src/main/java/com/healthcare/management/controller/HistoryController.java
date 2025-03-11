package com.healthcare.management.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.management.dto.HistoryDto;
//import com.healthcare.management.entity.MedicalHistory;
import com.healthcare.management.service.HistoryService;

@RestController
@RequestMapping("/history")
public class HistoryController {
	
	@Autowired
	private HistoryService historyService;
	
	@PostMapping("/add")
	public ResponseEntity<HistoryDto> addMedicalHistory(@RequestBody @Valid HistoryDto historyDto) {
		HistoryDto createdHistory =  historyService.addHistory(historyDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdHistory);
	}
	
	@GetMapping("/{patientId}")
	public ResponseEntity<HistoryDto> getHistory(@PathVariable String patientId) {
		HistoryDto history =  historyService.getHistoryByPatientId(patientId);
		return ResponseEntity.ok(history);
	}
	
	@GetMapping("/his/{historyId}")
	public ResponseEntity<HistoryDto> getHistoryByHistoryId(@PathVariable long historyId) {
		HistoryDto history =  historyService.getMedicalHistoryByHistoryId(historyId);
		return ResponseEntity.ok(history);
	}
	
	@PutMapping("/{patientId}")
	public ResponseEntity<HistoryDto> updateHistory(@PathVariable String patientId,@RequestBody @Valid HistoryDto historyDto) {
		HistoryDto updatedHistory =  historyService.updateMedicalHistory(patientId,historyDto);
		return ResponseEntity.ok(updatedHistory);
	}
	
	@DeleteMapping("/delete/{patientID}")
	public void deleteHistory(@PathVariable String patientID) {
        historyService.deleteMedicalHistory(patientID);
        
    }
	
	
	
	
}
