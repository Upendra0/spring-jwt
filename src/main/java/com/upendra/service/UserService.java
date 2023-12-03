package com.upendra.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upendra.model.User;
import com.upendra.repository.UserRepository;
import com.upendra.model.RefreshToken;
import com.upendra.repository.RefreshTokenRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	Logger logger = LoggerFactory.getLogger(UserService.class);

	@Transactional
	public void create(User user) {

		if (userRepository.getByEmail(user.getEmail()) != null)
			throw new EntityExistsException("User already Exist with email:" + user.getEmail());

		// Encode the password before saving to db
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.create(user);
	}

	@Transactional(readOnly = true)
	public Optional<User> readByEmail(String email) {
		return Optional.ofNullable(userRepository.getByEmail(email));
	}

	@Transactional
	public void update(User user) {
		if (userRepository.getByEmail(user.getEmail()) == null)
			throw new EntityNotFoundException("User not exist in database");

		// Encode the password before saving to db if password field is present.
		if(user.getPassword()!=null)
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		Map<String, Object> updatabaleFields = new HashMap<>();
		if(user.getPassword()!=null)
			updatabaleFields.put("password", user.getPassword());
		if(user.getFirstName()!=null)
			updatabaleFields.put("firstName", user.getFirstName());
		if(user.getLastName()!=null)
			updatabaleFields.put("lastName", user.getLastName());
		userRepository.updateByField(updatabaleFields, "email", user.getEmail());
	}

	@Transactional
	public void delete(Long id) {

		userRepository.delete(id);
	}

	@Transactional
	public void deleteByEmail(String email) {
		User user = userRepository.getByEmail(email);
		if (user != null) {
			RefreshToken refreshToken = refreshTokenRepository.findByUser(user);
			if (refreshToken != null) {
				refreshTokenRepository.delete(refreshToken.getId());
			}
			userRepository.deleteByField("email", email);
		}
		else{
			throw new EntityNotFoundException("User not found");
		}
	}

}
