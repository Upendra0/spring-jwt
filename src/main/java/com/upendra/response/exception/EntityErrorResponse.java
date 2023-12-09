package com.upendra.response.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class EntityErrorResponse extends ErrorResponse {

    public EntityErrorResponse(EntityExistsException ex){
        super(HttpStatus.BAD_REQUEST,"EntityExists");
        this.addError(ex.getMessage());
    }

    public EntityErrorResponse(EntityNotFoundException ex){
        super(HttpStatus.NOT_FOUND,"EntityNotFound");
        this.addError(ex.getMessage());
    }
}
