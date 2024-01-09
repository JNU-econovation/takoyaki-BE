package com.bestbenefits.takoyaki.exception;

public enum ExceptionCode {

    /******** Standard Exceptions ********/
    HTTP_MSG_NOT_READABLE("Request Body가 바르지 않습니다."),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED("지원하지 않는 HTTP Request 메서드입니다."),
    VALIDATION_FAILED("Validation을 실패했습니다."),
    MISSING_SERVLET_REQUEST_PARAMETER("파라미터를 입력하지 않았습니다."),
    ILLEGAL_ARGUMENT("Illegal argument."),
    ILLEGAL_STATE("Illegal state."),
    NULL_POINTER_EXCEPTION("Null pointer."),
    NO_RESOURCE_FOUND("리소스가 존재하지 않습니다."),
    WEB_CLIENT_EXCEPTION("Request to other server failed."),
    JSON_PROCESSING_FAILED("JSON 파싱을 실패했습니다."),



    /********* User Controller Exceptions *********/
    UNAUTHORIZED("비인가 접근입니다. 로그인이 필요합니다."),
    NOT_FOUND_USER("사용자가 존재하지 않습니다."),
    INVALID_TYPE_VALUE("유효하지 않은 타입 값입니다."),
    DUPLICATE_NICKNAME("중복된 닉네임입니다."),
    ADDITIONAL_INFO_PROVIDED("이미 추가 정보가 제공되었습니다."),
    NICKNAME_CHANGE_TOO_EARLY("닉네임은 하루에 한 번만 바꿀 수 있습니다."),


    /********* Party Controller Exceptions *********/
    NOT_FOUND_PARTY("파티가 존재하지 않습니다."),
    NOT_TAKO_EXCEPTION("파티의 타코가 아닙니다."),
    PARTY_CLOSED("마감된 파티입니다."),
    CATEGORY_NOT_MODIFIABLE("카테고리는 수정할 수 없습니다."),
    MODIFIED_RECRUIT_NUMBER_NOT_BIGGER("수정 후 모집인원은 기존보다 같거나 커야 합니다."),
    MODIFIED_PLANNED_CLOSING_DATE_NOT_BEFORE("예상 마감 일시는 예상 시작 일시보다 이전이어야 합니다."),





    END_LINE("ExceptionCode End Line");
    private final String msg;
    ExceptionCode(String msg) { this.msg = msg; }
    public String getMsg() { return msg; }
}
