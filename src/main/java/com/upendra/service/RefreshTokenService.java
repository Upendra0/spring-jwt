package com.upendra.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upendra.model.RefreshToken;
import com.upendra.repository.RefreshTokenRepository;
import com.upendra.model.User;
import com.upendra.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RefreshTokenService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	@Value("${jwt.refresh.token.expiry}")
	Long expiryDurationInDays;

	@Transactional
	public RefreshToken createRefreshToken(String email) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(getRandomToken());
		refreshToken.setUser(userRepository.getByEmail(email));
		refreshToken.setExpiryTime(Instant.now().plus(Duration.ofDays(expiryDurationInDays)));
		
		refreshTokenRepository.create(refreshToken);
		return refreshToken;
	}
	
	@Transactional
	public RefreshToken getOrCreateRefreshToken(User user) {
		RefreshToken refreshToken = refreshTokenRepository.findByUser(user);
		if(refreshToken==null) {
			refreshToken = createRefreshToken(user.getEmail());
		}
		return refreshToken;
	}

	@Transactional
	public RefreshToken findByToken(String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
		if(refreshToken==null)
			throw new EntityNotFoundException("Refresh Token Not Found");
		return refreshToken;
	}

	@Transactional
	public void updateRefreshToken(RefreshToken refreshToken) {
		refreshToken.setToken(getRandomToken());
		refreshToken.setExpiryTime(Instant.now().plus(Duration.ofDays(expiryDurationInDays)));

		Map<String, Object> updatabaleFields = new HashMap<>();
		updatabaleFields.put("token", refreshToken.getToken());
		updatabaleFields.put("expiryTime", refreshToken.getExpiryTime());

		refreshTokenRepository.update(updatabaleFields, refreshToken.getId());;
	}

	@Transactional
	public void deleteByEmail(String email){
		User user = userRepository.getByEmail(email);
		if(user!=null){
			refreshTokenRepository.deleteByField("user", user);
		}
	}

	// Verify that a refresh token is valid by comapring its expiration time.
	public void verifyRefreshToken(RefreshToken refreshToken) {
		if(Instant.now().isAfter(refreshToken.getExpiryTime())) {
			throw new BadCredentialsException("Refresh Token Expired");
		}
	}

	public String getRandomToken(){
		return UUID.randomUUID().toString();
	}
}
