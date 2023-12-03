package com.upendra.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upendra.validation.onCreate;
import com.upendra.validation.onUpdate;
import com.upendra.dto.UserRequestDTO;
import com.upendra.response.UserResponse;
import com.upendra.model.Role;
import com.upendra.model.User;
import com.upendra.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
@Validated
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	
	@GetMapping("/user/{email}")
    @PreAuthorize("#email == authentication.getName()")
	public ResponseEntity<UserResponse> one(@PathVariable("email") String email, Authentication authentication) {
		logger.debug("Authentication principles:" + authentication.getName());
		User user = userService.readByEmail(email).orElse(null);
		if(user==null)
			throw new EntityNotFoundException("User not found with this email"+email);
		return ResponseEntity.ok(new UserResponse(user));
	}
	
	@PostMapping("/user")
	@Validated(onCreate.class)
	public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequestDTO userDTO){
		User user = userDTO.convertToUser();
		
		// set default role to USER
		user.setRole(Role.USER);
		
		userService.create(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user));
	}
	
	@PutMapping("/user")
    @PreAuthorize("#userDTO.getEmail() == authentication.getName()")
	@Validated(onUpdate.class)
	public ResponseEntity<UserResponse> update(@Valid @RequestBody UserRequestDTO userDTO, Authentication authentication){
		User user = userDTO.convertToUser();
		userService.update(user);
		return ResponseEntity.ok(new UserResponse(user));
	}
	
	@DeleteMapping("/user/{email}")
	@PreAuthorize("#email == authentication.getName()")
	public void delete(@PathVariable String email, Authentication authentication) {
		userService.deleteByEmail(email);
		ResponseEntity.ok();
	}
}
