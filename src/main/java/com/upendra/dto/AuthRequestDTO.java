package com.upendra.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthRequestDTO {

	@Email
	public String email;
	
	@NotEmpty
	@Length(min = 8, max = 20)
	public String password;

}
