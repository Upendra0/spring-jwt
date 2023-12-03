package com.upendra.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class AuthRequest{

	@Email
	public String email;
	
	@NotEmpty
	@Length(min = 8, max = 20)
	public String password;
	
	public AuthRequest() {}

	public AuthRequest(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
