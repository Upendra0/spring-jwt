package com.upendra.controller;

import com.upendra.dto.UserRequestDTO;
import com.upendra.model.User;
import com.upendra.response.ResponseBody;
import com.upendra.response.UserResponse;
import com.upendra.service.RefreshTokenService;
import com.upendra.service.UserService;
import com.upendra.validation.onCreate;
import com.upendra.validation.onUpdate;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
@Validated
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;

	@Autowired
	RefreshTokenService refreshTokenService;
	
	@GetMapping("/user/{email}")
    @PreAuthorize("#email == authentication.getName()")
	public ResponseEntity<ResponseBody> one(@PathVariable("email") String email, Authentication authentication) {
		User user = userService
				.readByEmail(email)
				.orElseThrow(()->new EntityNotFoundException("User Not found with the given email"));
		return ResponseEntity.ok(ResponseBody.forGet(new UserResponse(user)));
	}
	
	@PostMapping("/user")
	@Validated(onCreate.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseBody> create(@Valid @RequestBody UserRequestDTO userDTO){
		User user = userDTO.convertToUser();
		userService.create(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(ResponseBody.forCreate(new UserResponse(user)));
	}
	
	@PutMapping("/user")
    @PreAuthorize("#userDTO.getEmail() == authentication.getName()")
	@Validated(onUpdate.class)
	public ResponseEntity<ResponseBody> update(@Valid @RequestBody UserRequestDTO userDTO, Authentication authentication){
		User user = userDTO.convertToUser();
		userService.update(user);
		return ResponseEntity.ok(ResponseBody.forUpdate());
	}
	
	@DeleteMapping("/user/{email}")
	@PreAuthorize("#email == authentication.getName()")
	public ResponseEntity<ResponseBody> delete(@PathVariable String email, Authentication authentication) {
		refreshTokenService.deleteByEmail(email);
		userService.delete(email);
		return ResponseEntity.ok((ResponseBody.forDelete()));
	}
}
