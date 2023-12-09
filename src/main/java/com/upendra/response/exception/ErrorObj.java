package com.upendra.response.exception;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorObj {

    private String fieldName;
    private String message;
}
