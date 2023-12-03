package com.upendra.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthResponse{

	private String accessToken;
	private String refreshToken;
	
}
