package com.dataBase.automate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
**
* Exception when Specialization is not found.
* 
* @Author Sanjay R
* @Since 2025-03-18
*/
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SpecializationNotFoundException extends RuntimeException {
	public SpecializationNotFoundException(String msg) {
		super(msg);
	}

}
