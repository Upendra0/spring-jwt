package com.upendra.response.exception;

import org.springframework.http.HttpStatus;

public class AuthorisationDeniedErrorResponse extends ErrorResponse {

    public AuthorisationDeniedErrorResponse(Exception exception){
        super(HttpStatus.UNAUTHORIZED, "AuthorisationDenied");
        this.addError(exception.getMessage());
    }
}
