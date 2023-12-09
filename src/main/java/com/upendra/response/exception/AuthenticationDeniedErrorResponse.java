package com.upendra.response.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationDeniedErrorResponse extends ErrorResponse {

    public AuthenticationDeniedErrorResponse(Exception exception){
       super(HttpStatus.UNAUTHORIZED, "AuthenticationDenied");
       this.addError(exception.getMessage());
    }
}
