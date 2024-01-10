package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.ResponseEntityCreator;
import com.bestbenefits.takoyaki.exception.ExceptionCode;
import com.bestbenefits.takoyaki.exception.yaki.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class YakiExceptionHandler {
    @ExceptionHandler(TakoNotAllowedException.class)
    public ResponseEntity<?> handleTakoNotAllowedException() {
        return ResponseEntityCreator.fail(ExceptionCode.TAKO_NOT_ALLOWED, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AlreadyAppliedException.class)
    public ResponseEntity<?> handleAlreadyAppliedException() {
        return ResponseEntityCreator.fail(ExceptionCode.ALREADY_APPLIED, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CancelApplicationNotAllowedException.class)
    public ResponseEntity<?> handleCancelApplicationNotAllowedException() {
        return ResponseEntityCreator.fail(ExceptionCode.CANCEL_APPLICATION_NOT_ALLOWED, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AlreadyAcceptedYakiException.class)
    public ResponseEntity<?> handleAlreadyAcceptedYakiException() {
        return ResponseEntityCreator.fail(ExceptionCode.ALREADY_ACCEPTED_YAKI, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotYakiException.class)
    public ResponseEntity<?> handleNotYakiException() {
        return ResponseEntityCreator.fail(ExceptionCode.NOT_YAKI, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LeavePartyNotAllowedException.class)
    public ResponseEntity<?> handleLeavePartyNotAllowedException() {
        return ResponseEntityCreator.fail(ExceptionCode.LEAVE_PARTY_NOT_ALLOWED, HttpStatus.FORBIDDEN);
    }


}
