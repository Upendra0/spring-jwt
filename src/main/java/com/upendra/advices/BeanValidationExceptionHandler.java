package com.upendra.advices;

import com.upendra.response.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import com.upendra.dto.ValidationErrorResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class BeanValidationExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
      error.addError(new ErrorResponse(violation.getPropertyPath().toString(), violation.getMessage()));
    }
    return error;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      error.addError(new ErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()));
    }
    if (e.getBindingResult().getGlobalErrors().size() > 0) {
      e.getBindingResult().getGlobalErrors().forEach(
        objectError -> error.addError(new ErrorResponse("Global ErrorResponse", objectError.getDefaultMessage()))
      );
    }
    return error;
  }

}
