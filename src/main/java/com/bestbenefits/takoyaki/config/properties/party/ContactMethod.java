package com.bestbenefits.takoyaki.config.properties.party;

import com.bestbenefits.takoyaki.exception.common.InvalidTypeValueException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum ContactMethod {
    KAKAO_OPENCHATTING( "카카오톡 오픈채팅"),
    EMAIL("이메일"),
    CELLPHONE_NUMBER("전화번호");

    private final String name;
    ContactMethod(String name){
        this.name = name;
    }

    public static ContactMethod fromName(String contactMethodName) {
        for (ContactMethod contactMethod : ContactMethod.values()) {
            if (contactMethod.getName().equals(contactMethodName))
                return contactMethod;
        }
        throw new InvalidTypeValueException("contact_method", contactMethodName);
    }
    public static List<String> toNameList(){
        return Arrays.stream(ContactMethod.values())
                .map(ContactMethod::getName)
                .collect(Collectors.toList());
    }
}
