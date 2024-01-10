package com.bestbenefits.takoyaki.config.apiresponse;

import com.bestbenefits.takoyaki.exception.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseEntityCreator {

    public static ResponseEntity<?> success() {
        return new ResponseEntity<>(new SuccessResponseBody<>(null, null), HttpStatus.OK);
    }

    public static ResponseEntity<?> success(HttpStatusCode status) {
        return new ResponseEntity<>(new SuccessResponseBody<>(null, null), status);
    }


    public static <D> ResponseEntity<?> success(D data) {
        return new ResponseEntity<>(new SuccessResponseBody<>(null, data), HttpStatus.OK);
    }


    public static <D> ResponseEntity<?> success(D data, HttpStatusCode status) {
        return new ResponseEntity<>(new SuccessResponseBody<>(null, data), status);
    }

    public static <M, D> ResponseEntity<?> success(M meta, D data) {
        return new ResponseEntity<>(new SuccessResponseBody<>(meta, data), HttpStatus.OK);
    }

    public static <M, D> ResponseEntity<?> success(M meta, D data, HttpStatusCode status) {
        return new ResponseEntity<>(new SuccessResponseBody<>(meta, data), status);
    }

    public static ResponseEntity<?> fail(ExceptionCode code) {
        return new ResponseEntity<>(new FailResponseBody(code), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> fail(ExceptionCode code, HttpStatusCode status) {
        return new ResponseEntity<>(new FailResponseBody(code), status);
    }

    public static ResponseEntity<?> fail(ExceptionCode code, String additional) {
        return new ResponseEntity<>(new FailResponseBody(code, additional), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> fail(ExceptionCode code, String additional, HttpStatusCode status) {
        return new ResponseEntity<>(new FailResponseBody(code, additional), status);
    }

    public static ResponseEntity<?> fail(ExceptionCode code, String additional, boolean custom) {
        return new ResponseEntity<>(new FailResponseBody(code, additional, custom), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> fail(ExceptionCode code, String additional, boolean custom, HttpStatusCode status) {
        return new ResponseEntity<>(new FailResponseBody(code, additional, custom), status);
    }
}
