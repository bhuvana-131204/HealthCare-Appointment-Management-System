package com.users.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.users.app.enums.Specialization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
	private String doctor_id;
	private String name;
	private String email;
	private String phoneNumber;
	private Specialization specialization;
	 @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
}
