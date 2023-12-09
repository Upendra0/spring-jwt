package com.upendra.ExceptionHandler;

import com.upendra.response.exception.AuthenticationDeniedErrorResponse;
import com.upendra.response.exception.AuthorisationDeniedErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(AuthenticationExceptionHandler.class);
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler
	public AuthenticationDeniedErrorResponse AuthenticationDeniedExceptionHandler(HttpServletResponse response, BadCredentialsException ex) {
		logger.error(ex.getMessage());
		logger.trace(Arrays.toString(ex.getStackTrace()));
		response.addHeader(HttpHeaders.WWW_AUTHENTICATE,"Bearer");
        return new AuthenticationDeniedErrorResponse(ex);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler
	public AuthorisationDeniedErrorResponse AuthorizationDeniedExceptionHandler(AccessDeniedException ex) {
		logger.error(ex.getMessage());
		logger.trace(Arrays.toString(ex.getStackTrace()));
		return new AuthorisationDeniedErrorResponse(ex);
	}

}
