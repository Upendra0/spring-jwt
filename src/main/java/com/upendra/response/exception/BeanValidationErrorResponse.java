package com.upendra.response.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BeanValidationErrorResponse extends ErrorResponse {
    private static final String errorCode = "ValidationFailed";
    public BeanValidationErrorResponse(ConstraintViolationException ex){
        super(HttpStatus.BAD_REQUEST, errorCode);
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            this.addError(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }

    public BeanValidationErrorResponse(MethodArgumentNotValidException ex){
        super(HttpStatus.BAD_REQUEST,errorCode);
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            this.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        if (!ex.getBindingResult().getGlobalErrors().isEmpty()) {
            for(ObjectError error : ex.getBindingResult().getGlobalErrors()){
                this.addError(error.getDefaultMessage());
            }
        }
    }
}
