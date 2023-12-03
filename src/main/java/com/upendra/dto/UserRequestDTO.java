package com.upendra.dto;

import org.hibernate.validator.constraints.Length;

import com.upendra.validation.AtLeastOneNotNull;
import com.upendra.validation.onCreate;
import com.upendra.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@AtLeastOneNotNull(fieldNames = {"firstName", "lastName", "password"}, message = "Atlest one of firstName, lastName, password must be send to update")
public class UserRequestDTO {
	
	@Email
	@NotBlank
	private String email;
	
	@NotBlank(groups=onCreate.class)
	@Length(min = 8, max = 20)
	private String password;
	
	@NotBlank(groups = onCreate.class)
	@Length(min = 1, max = 20)
	private String firstName;
	
	@NotBlank(groups = onCreate.class)
	@Length(min = 1, max = 20)
	private String lastName;
		
	public User convertToUser() {
		User user = new User();
		user.setEmail(this.email);
		user.setPassword(this.password);
		user.setFirstName(this.firstName);
		user.setLastName(this.lastName);
		return user;
	}
}
