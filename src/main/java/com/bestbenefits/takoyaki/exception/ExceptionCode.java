package com.bestbenefits.takoyaki.exception;

public enum ExceptionCode {
    NEED_LOGIN("비인가 접근입니다. 로그인이 필요합니다."),
    INVALID_ID("콘텐츠의 ID 값이 유효하지 않거나 존재하지 않습니다.");



    private final String msg;
    ExceptionCode(String msg) { this.msg = msg; }
    public String getMsg() { return msg; }
}
