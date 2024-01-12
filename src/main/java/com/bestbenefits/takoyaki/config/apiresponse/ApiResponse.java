package com.bestbenefits.takoyaki.config.apiresponse;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ApiResponse <T> extends ResponseEntity<T> {
    public ApiResponse(T body, HttpStatusCode status) {
        super(body, status);
    }
}
