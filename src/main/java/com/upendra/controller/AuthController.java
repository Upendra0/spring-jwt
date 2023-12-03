package com.upendra.controller;

import com.upendra.dto.AuthRequest;
import com.upendra.response.AuthResponse;
import com.upendra.dto.RefreshTokenRequest;
import com.upendra.model.RefreshToken;
import com.upendra.service.AuthService;
import com.upendra.service.BlockedTokenCacheService;
import com.upendra.service.JwtService;
import com.upendra.service.RefreshTokenService;
import com.upendra.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping(value = "/api/v1/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	AuthService authService;
	
	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	BlockedTokenCacheService blockedTokenCacheService;

	private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
				
		//1. Verify the credentials, throws bad credential exception if not matches
		User user = authService.verifyCredentials(authRequest.getEmail(), authRequest.getPassword());
		
		
		//2. Generate access token
		String accessToken = jwtService.generateToken(user.getEmail(), user.getRole().toString());
		
		//3. Generate refresh token if token is not in database.
		RefreshToken refreshToken = refreshTokenService.getOrCreateRefreshToken(user);
		return ResponseEntity.ok(new AuthResponse(accessToken,refreshToken.getToken()));
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request,Authentication authentication) {
		// delete the refresh token
		String email = authentication.getName();
		refreshTokenService.deleteByEmail(email);

		// add the access token in guava expiry map.
		String accessToken = bearerTokenResolver.resolve(request);
		blockedTokenCacheService.blockToken(accessToken);
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.getToken());
		refreshTokenService.verifyRefreshToken(refreshToken);

		// Generate new access and refresh token
		String accessToken = jwtService.generateToken(refreshToken.getUser().getEmail(), refreshToken.getUser().getEmail());

		// Update the token and expiry for refresh token
		refreshTokenService.updateRefreshToken(refreshToken);
		
		return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken()));
	}
}
