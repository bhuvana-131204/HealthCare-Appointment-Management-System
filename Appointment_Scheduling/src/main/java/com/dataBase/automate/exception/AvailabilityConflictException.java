package com.dataBase.automate.exception;
/*
**
* Exception when Availability is in conflict.
* 
* @Author Sanjay R
* @Since 2025-03-18
*/
public class AvailabilityConflictException extends RuntimeException {
	public AvailabilityConflictException(String msg) {
		super(msg);
	}

}
