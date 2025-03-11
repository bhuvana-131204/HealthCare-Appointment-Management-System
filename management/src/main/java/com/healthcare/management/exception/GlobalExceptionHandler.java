package com.healthcare.management.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(NoConsultationDetailsFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNoConsultationDetailsFoundException(NoConsultationDetailsFoundException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(NoHistoryFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNoHistoryFoundException(NoHistoryFoundException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(NoAppointmentFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNoAppointmentFoundException(NoAppointmentFoundException ex) {
		return ex.getMessage();
	}
	

	@ExceptionHandler(NoPatientFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNoPatientFoundException(NoPatientFoundException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleValidationException(MethodArgumentNotValidException exec) {
		return exec.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.joining(", "));
	}
	
	
}
