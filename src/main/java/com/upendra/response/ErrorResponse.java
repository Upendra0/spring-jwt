package com.upendra.response;

import lombok.*;
import org.springframework.web.service.annotation.GetExchange;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {
	
	private String fieldName;
	
	private String message;
}
