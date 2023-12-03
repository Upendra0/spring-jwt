package com.upendra.dto;

import java.util.ArrayList;
import java.util.List;

import com.upendra.response.ErrorResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidationErrorResponse {
	
	 private List<ErrorResponse> errorResponses;
	 
	 public ValidationErrorResponse () {
		 this.errorResponses = new ArrayList<>();
	 }

	public ValidationErrorResponse(List<ErrorResponse> errorResponses) {
		super();
		this.errorResponses = errorResponses;
	}

	public void addError(ErrorResponse ErrorResponse) {
		errorResponses.add(ErrorResponse);
	}

}
