package com.bestbenefits.takoyaki.exception;

public class InvalidIdValueException extends IllegalArgumentException {
    public InvalidIdValueException() {
        super("Invalid_ID: 콘텐츠의 ID 값이 유효하지 않거나 존재하지 않습니다.");
    }
}
