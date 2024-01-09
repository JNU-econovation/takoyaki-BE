package com.bestbenefits.takoyaki.exception;

public enum ExceptionCode {
    //Standard Exception Codes
    HTTP_MSG_NOT_READABLE("Request Body가 바르지 않습니다."),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED("지원하지 않는 HTTP Request 메서드입니다."),
    INVALID_METHOD_ARGUMENT("Validation을 실패했습니다."),
    MISSING_SERVLET_REQUEST_PARAMETER("파라미터를 입력하지 않았습니다."),
    ILLEGAL_ARGUMENT("Illegal argument."),
    ILLEGAL_STATE("Illegal state."),
    NULL_POINTER_EXCEPTION("Null pointer."),
    NO_RESOURCE_FOUND("리소스가 존재하지 않습니다."),
    WEB_CLIENT_EXCEPTION("Request to other server failed."),
    JSON_PROCESSING_FAILED("JSON 파싱을 실패했습니다."),




    UNAUTHORIZED("비인가 접근입니다. 로그인이 필요합니다."),
    INVALID_ID("콘텐츠의 ID 값이 유효하지 않거나 존재하지 않습니다.");



    private final String msg;
    ExceptionCode(String msg) { this.msg = msg; }
    public String getMsg() { return msg; }
}
