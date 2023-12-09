package com.upendra.service;

import com.upendra.model.Role;
import com.upendra.model.User;
import com.upendra.repository.RefreshTokenRepository;
import com.upendra.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

//	@Transactional
	public void create(User user) {

		if (userRepository.existsByEmail(user.getEmail()))
			throw new EntityExistsException("User already Exist with email:" + user.getEmail());

		// Encode the password before saving to db
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		// set default role as User
		user.setRole(Role.USER);
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public Optional<User> readByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Transactional
	public void update(User user) {
		User existingUser = userRepository.findByEmail(user.getEmail())
				.orElseThrow(()->new EntityNotFoundException("User not found"));
		// Update the fields
		if(user.getPassword()!=null)
			existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		if(user.getFirstName()!=null)
			existingUser.setFirstName(user.getFirstName());
		if(user.getLastName()!=null)
			existingUser.setLastName(user.getLastName());
		userRepository.save(existingUser);
	}

	@Transactional
	public void delete(String email) {
		userRepository.deleteByEmail(email);
	}

}
