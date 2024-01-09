package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.ResponseEntityCreator;
import com.bestbenefits.takoyaki.exception.ExceptionCode;
import com.bestbenefits.takoyaki.exception.InvalidIdValueException;
import com.bestbenefits.takoyaki.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleNeedLoginException() {
        return ResponseEntityCreator.fail(ExceptionCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidIdValueException.class)
    public ResponseEntity<?> handleInvalidIdValueException() {
        return ResponseEntityCreator.fail(ExceptionCode.INVALID_ID, HttpStatus.NOT_FOUND);
    }
}
