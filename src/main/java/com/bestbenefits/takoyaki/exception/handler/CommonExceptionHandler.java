package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.ResponseEntityCreator;
import com.bestbenefits.takoyaki.exception.ExceptionCode;
import com.bestbenefits.takoyaki.exception.common.InvalidTypeValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    //제한된 문자열만 수용할 수 있는 파라미터에 범위 밖의 유효하지 않은 문자열 제공
    @ExceptionHandler(InvalidTypeValueException.class)
    public ResponseEntity<?> handleInvalidTypeValueException(InvalidTypeValueException e) {
        String msg = String.format("%s: '%s' 값은 %s", e.getTypeName(), e.getProvided(), ExceptionCode.INVALID_TYPE_VALUE.getMsg());
        return ResponseEntityCreator.fail(
                ExceptionCode.INVALID_TYPE_VALUE, msg, true);
    }
}
