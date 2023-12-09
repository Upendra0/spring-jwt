package com.upendra.response.exception;

import org.springframework.http.HttpStatus;

public class ServerErrorErrorResponse extends ErrorResponse {

    public ServerErrorErrorResponse(Exception ex){
        super(HttpStatus.INTERNAL_SERVER_ERROR, "InternalServerError");
        this.addError(ex.getMessage());
    }
}
