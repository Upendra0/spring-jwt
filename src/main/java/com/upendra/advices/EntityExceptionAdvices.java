package com.upendra.advices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class EntityExceptionAdvices {
	
	private Logger logger = LoggerFactory.getLogger(GenricExceptionHandler.class);	
	
	@ExceptionHandler(EntityExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String EntityExistsExceptionHandler(Exception ex) {
		logger.error(ex.getMessage());
		logger.trace(ex.getStackTrace().toString());
		return ex.getMessage();
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String EntityNotFoundExceptionHandler(Exception ex) {
		logger.error(ex.getMessage());
		logger.trace(ex.getStackTrace().toString());
		return ex.getMessage();
	}
}
