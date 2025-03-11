package com.example.demo.DTO;

import com.example.demo.enums.Specialization;

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
	private String password;
}
