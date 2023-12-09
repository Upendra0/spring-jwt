package com.upendra.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseBody {

    private int status;
    private Object data;

    public static ResponseBody forCreate(Object data){
        return new ResponseBody(HttpStatus.CREATED.value(), data);
    }

    public static ResponseBody forGet(Object data){
        return new ResponseBody(HttpStatus.OK.value(), data);
    }

    public static ResponseBody forUpdate(){
        return new ResponseBody(HttpStatus.OK.value(), null);
    }

    public static ResponseBody forDelete(){
        return new ResponseBody(HttpStatus.OK.value(), null);
    }
}
