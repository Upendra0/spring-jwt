package com.upendra.ExceptionHandler;

import com.upendra.response.exception.BeanValidationErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class BeanValidationExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(BeanValidationExceptionHandler.class);

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  BeanValidationErrorResponse onConstraintValidationException(ConstraintViolationException ex) {
    logger.error(ex.getMessage());
    logger.trace(Arrays.toString(ex.getStackTrace()));
    return new BeanValidationErrorResponse(ex);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  BeanValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    logger.error(ex.getMessage());
    logger.trace(Arrays.toString(ex.getStackTrace()));
    return new BeanValidationErrorResponse(ex);
  }

}
