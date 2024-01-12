package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.exception.ExceptionCode;
import com.bestbenefits.takoyaki.exception.party.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PartyControllerExceptionHandler {
    @ExceptionHandler(PartyNotFoundException.class)
    public ApiResponse<?> handlePartyNotFoundException() {
        return ApiResponseCreator.fail(ExceptionCode.PARTY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotTakoException.class)
    public ApiResponse<?> handleNotAuthorException() {
        return ApiResponseCreator.fail(ExceptionCode.NOT_TAKO, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(PartyClosedException.class)
    public ApiResponse<?> handlePartyClosedException() {
        return ApiResponseCreator.fail(ExceptionCode.PARTY_CLOSED, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CategoryNotModifiableException.class)
    public ApiResponse<?> handleCategoryNotModifiableException() {
        return ApiResponseCreator.fail(ExceptionCode.CATEGORY_NOT_MODIFIABLE, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ModifiedRecruitNumberNotBiggerException.class)
    public ApiResponse<?> handleModifiedRecruitNumberNotBiggerException() {
        return ApiResponseCreator.fail(ExceptionCode.MODIFIED_RECRUIT_NUMBER_NOT_BIGGER, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ModifiedPlannedClosingDateNotBeforeException.class)
    public ApiResponse<?> handleModifiedPlannedClosingDateNotBeforeException() {
        return ApiResponseCreator.fail(ExceptionCode.MODIFIED_PLANNED_CLOSING_DATE_NOT_BEFORE, HttpStatus.FORBIDDEN);
    }
}
