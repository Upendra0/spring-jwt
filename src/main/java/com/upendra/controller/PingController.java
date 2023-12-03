package com.upendra.controller;

import com.upendra.response.PingResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class PingController {

    @GetMapping("/ping")
    public ResponseEntity<PingResponse> ping(){
        return ResponseEntity.ok(new PingResponse("UP"));
    }
}
