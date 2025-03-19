package com.users.app.dto;

import com.users.app.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignInRequest {
	private String Identifier;
	private String Password;
	Role role;
}
