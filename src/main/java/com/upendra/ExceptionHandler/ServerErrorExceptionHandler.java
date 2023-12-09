package com.upendra.ExceptionHandler;

import com.upendra.response.exception.ServerErrorErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestControllerAdvice
public class ServerErrorExceptionHandler {
	
	private final Logger logger = LoggerFactory.getLogger(ServerErrorExceptionHandler.class);
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ServerErrorErrorResponse ServerExceptionHandler(Exception ex) {
		logger.error(ex.getMessage());
		logger.trace(Arrays.toString(ex.getStackTrace()));
		return new ServerErrorErrorResponse(ex);
	}
}
