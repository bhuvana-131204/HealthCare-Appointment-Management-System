package com.example.demo.exceptions;

public class NoDoctorFoundException  extends RuntimeException{
	public NoDoctorFoundException(String message){
		 super(message);
	 }
}
