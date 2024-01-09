package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.ResponseEntityCreator;
import com.bestbenefits.takoyaki.exception.ExceptionCode;
import com.bestbenefits.takoyaki.exception.party.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PartyControllerExceptionHandler {
    @ExceptionHandler(NotFoundPartyException.class)
    public ResponseEntity<?> handleNotFoundPartyException() {
        return ResponseEntityCreator.fail(ExceptionCode.NOT_FOUND_PARTY, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotTakoException.class)
    public ResponseEntity<?> handleNotAuthorException() {
        return ResponseEntityCreator.fail(ExceptionCode.NOT_TAKO_EXCEPTION, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(PartyClosedException.class)
    public ResponseEntity<?> handlePartyClosedException() {
        return ResponseEntityCreator.fail(ExceptionCode.PARTY_CLOSED, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CategoryNotModifiableException.class)
    public ResponseEntity<?> handleCategoryNotModifiableException() {
        return ResponseEntityCreator.fail(ExceptionCode.CATEGORY_NOT_MODIFIABLE, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ModifiedRecruitNumberNotBiggerException.class)
    public ResponseEntity<?> handleModifiedRecruitNumberNotBiggerException() {
        return ResponseEntityCreator.fail(ExceptionCode.CATEGORY_NOT_MODIFIABLE, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ModifiedPlannedClosingDateNotBeforeException.class)
    public ResponseEntity<?> handleModifiedPlannedClosingDateNotBeforeException() {
        return ResponseEntityCreator.fail(ExceptionCode.MODIFIED_PLANNED_CLOSING_DATE_NOT_BEFORE, HttpStatus.FORBIDDEN);
    }
}
