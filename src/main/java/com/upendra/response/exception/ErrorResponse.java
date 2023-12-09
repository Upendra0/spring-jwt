package com.upendra.response.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponse {

	private int status;
	private String errorCode;
	private List<ErrorObj> errors;
	private static final String none = "null";

	public ErrorResponse(HttpStatus status, String errorCode){
		this.status = status.value();
		this.errorCode = errorCode;
		this.errors = new ArrayList<>();
	}

	public void addError(ErrorObj error){
		errors.add(error);
	}

	public void addError(String fieldName, String message){
		addError(new ErrorObj(fieldName, message));
	}

	public void addError(String message){
		addError(new ErrorObj(none,message));
	}
}
