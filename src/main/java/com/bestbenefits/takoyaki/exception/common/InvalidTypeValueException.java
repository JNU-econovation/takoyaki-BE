package com.bestbenefits.takoyaki.exception.common;

//일정한 타입이 있는 문자열에 타입에 없는 문자열을 제공함
public class InvalidTypeValueException extends RuntimeException {
    private final String typeName;
    private final String provided;

    public InvalidTypeValueException(String typeName, String provided) {
        this.typeName = typeName;
        this.provided = provided;
    }

    public String getTypeName() {
        return typeName;
    }
    public String getProvided() {
        return provided;
    }
}
