package com.upendra.service;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.stereotype.Service;

import com.upendra.utils.DayToMillisConverter;

@Service
public class JwtService {

	@Value("${jwt.access.token.expiry}")
	private long expiryDays;
	
	BearerTokenAuthenticationFilter filter;
	
	@Autowired
	private JwtEncoder encoder;
	
	public String generateToken(String subject, String scope) {
		Instant now = Instant.now();
		
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(DayToMillisConverter.convert(expiryDays)))
				.subject(subject)
				.claim("scope", scope)
				.build();
		return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
	
	public void blockToken() {
		
	}
	
	public boolean isTokenBlocked(String token) {
		return true;
	}
}
