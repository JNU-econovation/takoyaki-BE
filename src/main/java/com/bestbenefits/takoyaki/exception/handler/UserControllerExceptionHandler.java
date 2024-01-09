package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.ResponseEntityCreator;
import com.bestbenefits.takoyaki.exception.*;
import com.bestbenefits.takoyaki.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerExceptionHandler {
    //로그인 필요한 API를 비로그인 상태에서 요청
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException() {
        return ResponseEntityCreator.fail(ExceptionCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    //사용자를 찾을 수 없을 때 발생
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException() {
        return ResponseEntityCreator.fail(ExceptionCode.NOT_FOUND_USER, HttpStatus.NOT_FOUND);
    }

    //제한된 문자열만 수용할 수 있는 파라미터에 범위 밖의 유효하지 않은 문자열 제공
    @ExceptionHandler(InvalidTypeValueException.class)
    public ResponseEntity<?> handleInvalidTypeValueException() {
        return ResponseEntityCreator.fail(ExceptionCode.INVALID_TYPE_VALUE);
    }

    //중복된 닉네임 제공
    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<?> handleDuplicateNicknameException() {
        return ResponseEntityCreator.fail(ExceptionCode.DUPLICATE_NICKNAME, HttpStatus.CONFLICT);
    }

    //이미 추가 정보를 받음
    @ExceptionHandler(AdditionalInfoProvidedException.class)
    public ResponseEntity<?> handleAdditionalInfoProvidedException() {
        return ResponseEntityCreator.fail(ExceptionCode.ADDITIONAL_INFO_PROVIDED, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NicknameChangeTooEarlyException.class)
    public ResponseEntity<?> handleNicknameChangeTooEarlyException() {
        return ResponseEntityCreator.fail(ExceptionCode.NICKNAME_CHANGE_TOO_EARLY);
    }
}
