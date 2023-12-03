package com.upendra.config;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.upendra.filter.JwtTokenBlockedAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the main application.
 *
 * @author Upendra Kumar
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

	@Value("${jwt.public.key}")
	RSAPublicKey key;

	@Value("${jwt.private.key}")
	RSAPrivateKey priv;

	@Autowired
	@Lazy
	JwtTokenBlockedAuthenticationFilter jwtTokenBlockedAuthenticationFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorize) -> {
				authorize
					.requestMatchers("/api/v1/auth/logout").authenticated()
					.requestMatchers("/api/v1/auth/*").permitAll()
					.requestMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
						.requestMatchers(HttpMethod.GET, "/ping").permitAll()
					.anyRequest().authenticated();
				})
			.csrf(AbstractHttpConfigurer::disable)
			.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling((exceptions) -> {
				exceptions
				.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
				.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
			});
		
		http.addFilterAfter(jwtTokenBlockedAuthenticationFilter, BearerTokenAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(this.key).build();
	}

	@Bean
	JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}
	
	@Bean
	PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean 
	Cache<String, Boolean> blockedTokenCache() {
	    return CacheBuilder.newBuilder()
	        .expireAfterWrite(24, TimeUnit.HOURS)
	        .build();
	}

}