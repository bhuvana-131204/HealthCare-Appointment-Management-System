package com.users.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.users.app.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
	private String patient_id;
	private String name;
	private Gender gender;
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private Integer age;
	private String address;
	private String phoneNumber;
}
