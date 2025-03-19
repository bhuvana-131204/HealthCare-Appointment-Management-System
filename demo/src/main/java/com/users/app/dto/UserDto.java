package com.users.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.users.app.enums.Role;
import com.users.app.model.Doctor;
import com.users.app.model.Patient;

import lombok.Data;

@Data
public class UserDto {
	private String Id;
	private String email;
	private String phoneNumber;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private Doctor doctor;
	private Patient patient;
	private Role role;
}
