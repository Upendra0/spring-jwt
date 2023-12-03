package com.upendra.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.upendra.service.BlockedTokenCacheService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenBlockedAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	BlockedTokenCacheService blockedTokenCacheService;

	BearerTokenAuthenticationFilter filter;

	private Logger logger = LoggerFactory.getLogger(JwtTokenBlockedAuthenticationFilter.class);

	private BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();

	private AuthenticationEntryPoint authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 1. Extract the token from request header
		String token;
		try {
			token = this.bearerTokenResolver.resolve(request);
		}
		catch (OAuth2AuthenticationException invalid) {
			this.logger.trace("Sending to authentication entry point since failed to resolve bearer token", invalid);
			this.authenticationEntryPoint.commence(request, response, invalid);
			return;
		}

		// 2. if token is null, request not need authentication
		if (token == null) {
			this.logger.debug("Did not process request since did not find bearer token");
			filterChain.doFilter(request, response);
			return;
		}

		// 2. check if token is corrrect otherwise throw 401 Not authroised exception.
		if(blockedTokenCacheService.isTokenBlocked(token)){
			this.logger.debug("Token was blocked, so rejecting the authentication");
			AuthenticationException exception = new OAuth2AuthenticationException(HttpStatus.UNAUTHORIZED.name());
			this.authenticationEntryPoint.commence(request, response, exception);
			return ;
		}
		filterChain.doFilter(request, response);
	}

}
