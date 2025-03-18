package com.availabilitySchedule.exception;

public class UnavailableException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public UnavailableException(String message) {
        super(message);
    }
}
