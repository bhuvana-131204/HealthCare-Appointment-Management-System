package com.example.demo.DTO;

import com.example.demo.enums.Role;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;

import lombok.Data;

@Data
public class UserDto {
	private String Id;
	private String email;
	private String phoneNumber;
	private String password;
	private Doctor doctor;
	private Patient patient;
	private Role role;
}
