package com.example.demo.DTO;

import com.example.demo.enums.Gender;

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
	private String password;
	private Integer age;
	private String address;
	private String phoneNumber;
}
