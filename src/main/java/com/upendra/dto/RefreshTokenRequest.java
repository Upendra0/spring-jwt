package com.upendra.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {

	@NotBlank
	String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
