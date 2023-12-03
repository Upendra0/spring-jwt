package com.upendra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.upendra.model.User;

@Service
public class AuthService {
	
	@Autowired 
	UserService userService;
	
	@Autowired
	PasswordEncoder bCryptPasswordEncoder;
	
	public User verifyCredentials(String email, String password) {
		User user = userService.readByEmail(email).orElse(null);
		if(user==null || !bCryptPasswordEncoder.matches(password, user.getPassword()))
			throw new BadCredentialsException("Email or Password does not matches");
		else
			return user;
	}
}
