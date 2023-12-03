package com.upendra.advices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GenricExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(GenricExceptionHandler.class);

	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String ServerExceptionHandler(Exception ex) {
		logger.error(ex.getMessage());
		logger.trace(ex.getStackTrace().toString());
		return "Server ErrorResponse occured";
	}
}
