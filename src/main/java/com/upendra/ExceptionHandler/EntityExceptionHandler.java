package com.upendra.ExceptionHandler;

import com.upendra.response.exception.EntityErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.util.Arrays;

@RestControllerAdvice
public class EntityExceptionHandler {
	
	private final Logger logger = LoggerFactory.getLogger(ServerErrorExceptionHandler.class);
	
	@ExceptionHandler(EntityExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public EntityErrorResponse EntityExistsExceptionHandler(EntityExistsException ex) {
		logger.error(ex.getMessage());
		logger.trace(Arrays.toString(ex.getStackTrace()));
		return new EntityErrorResponse(ex);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public EntityErrorResponse EntityNotFoundExceptionHandler(EntityNotFoundException ex) {
		logger.error(ex.getMessage());
		logger.trace(Arrays.toString(ex.getStackTrace()));
		return new EntityErrorResponse(ex);
	}
}
