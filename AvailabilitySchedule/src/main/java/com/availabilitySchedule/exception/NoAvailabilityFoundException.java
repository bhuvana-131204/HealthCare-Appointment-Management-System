package com.availabilitySchedule.exception;

public class NoAvailabilityFoundException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public NoAvailabilityFoundException(String message) {
        super(message);
    }
}

