package com.dataBase.automate.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dataBase.automate.dto.Response;
import com.dataBase.automate.exception.AppointmentNotFoundException;
import com.dataBase.automate.exception.AvailabilityNotFoundException;
import com.dataBase.automate.exception.SpecializationNotFoundException;
import com.dataBase.automate.exception.TimeSlotNotFoundException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppointmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<?>> handleAppointmentNotFoundException(AppointmentNotFoundException ex) {
        log.error("Exception: {}", ex.getMessage());
        Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AvailabilityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<?>> handleAvailabilityNotFoundException(AvailabilityNotFoundException ex) {
        log.error("Exception: {}", ex.getMessage());
        Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TimeSlotNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<?>> handleTimeSlotNotFoundException(TimeSlotNotFoundException ex) {
        log.error("Exception: {}", ex.getMessage());
        Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SpecializationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<?>> handleSpecializationNotFoundException(SpecializationNotFoundException ex) {
        log.error("Exception: {}", ex.getMessage());
        Response<?> response = new Response<>(false, HttpStatus.NOT_FOUND, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}