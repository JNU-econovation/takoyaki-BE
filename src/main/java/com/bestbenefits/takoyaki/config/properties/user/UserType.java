package com.bestbenefits.takoyaki.config.properties.user;

import com.bestbenefits.takoyaki.config.properties.party.PartyListType;
import com.bestbenefits.takoyaki.exception.common.InvalidTypeValueException;
import lombok.Getter;

@Getter
public enum UserType {
    TAKO("tako"),
    YAKI("yaki"),
    OTHER("other"); //로그인 안된 사용자거나 로그인인데 타코도 야끼도 아닌경우

    private String name; //아직 사용은 안됨, 혹시 몰라서 추가

    UserType(String name){
        this.name = name;
    }

    public static UserType fromName(String name) {
        for (UserType userType : UserType.values()) {
            if (userType.getName().equalsIgnoreCase(name))
                return userType;
        }
        throw new InvalidTypeValueException("party_list_type", name);
    }
}
