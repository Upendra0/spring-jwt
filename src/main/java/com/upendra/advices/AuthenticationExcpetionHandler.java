package com.upendra.advices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class AuthenticationExcpetionHandler {

	private final Logger logger = LoggerFactory.getLogger(AuthenticationExcpetionHandler.class);
	
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler
	public ErrorResponseException AuthenticationDeniedExceptionhandler(BadCredentialsException ex) {
		logger.error(ex.getMessage());
        return new ErrorResponseException(HttpStatus.UNAUTHORIZED);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler
	public ErrorResponseException AuthroisationDeniedExceptionHandler(AccessDeniedException ex) {
		logger.error(ex.getMessage());
		logger.trace(ex.getStackTrace().toString());
		return new ErrorResponseException(HttpStatus.FORBIDDEN, ex);
	}

}
