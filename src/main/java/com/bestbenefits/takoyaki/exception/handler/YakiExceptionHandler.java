package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.exception.ExceptionCode;
import com.bestbenefits.takoyaki.exception.yaki.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class YakiExceptionHandler {
    @ExceptionHandler(TakoNotAllowedException.class)
    public ApiResponse<?> handleTakoNotAllowedException() {
        return ApiResponseCreator.fail(ExceptionCode.TAKO_NOT_ALLOWED, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AlreadyAppliedException.class)
    public ApiResponse<?> handleAlreadyAppliedException() {
        return ApiResponseCreator.fail(ExceptionCode.ALREADY_APPLIED, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CancelApplicationNotAllowedException.class)
    public ApiResponse<?> handleCancelApplicationNotAllowedException() {
        return ApiResponseCreator.fail(ExceptionCode.CANCEL_APPLICATION_NOT_ALLOWED, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AlreadyAcceptedYakiException.class)
    public ApiResponse<?> handleAlreadyAcceptedYakiException() {
        return ApiResponseCreator.fail(ExceptionCode.ALREADY_ACCEPTED_YAKI, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotYakiException.class)
    public ApiResponse<?> handleNotYakiException() {
        return ApiResponseCreator.fail(ExceptionCode.NOT_YAKI, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LeavePartyNotAllowedException.class)
    public ApiResponse<?> handleLeavePartyNotAllowedException() {
        return ApiResponseCreator.fail(ExceptionCode.LEAVE_PARTY_NOT_ALLOWED, HttpStatus.FORBIDDEN);
    }


}
