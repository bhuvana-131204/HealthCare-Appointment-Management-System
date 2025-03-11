package com.availabilitySchedule.controller;


import com.availabilitySchedule.dto.AvailabilityDTO;
import com.availabilitySchedule.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @PostMapping("/update")
    public ResponseEntity<String> updateAvailability(@RequestBody AvailabilityDTO availabilityDTO) {
        availabilityService.updateAvailability(availabilityDTO);
        return ResponseEntity.ok("Availability updated successfully");
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<AvailabilityDTO> getAvailability(@PathVariable Long doctorId) {
        return ResponseEntity.ok(availabilityService.getAvailability(doctorId));
    }
}