package com.bestbenefits.takoyaki.config.properties.user;

import com.bestbenefits.takoyaki.config.properties.party.PartyListType;
import com.bestbenefits.takoyaki.exception.common.InvalidTypeValueException;
import lombok.Getter;

@Getter
public enum UserType {
    TAKO("tako"),
    YAKI("yaki"),
    OTHER("other");

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
