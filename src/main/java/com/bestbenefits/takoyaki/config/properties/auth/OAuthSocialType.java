package com.bestbenefits.takoyaki.config.properties.auth;

import com.bestbenefits.takoyaki.exception.common.InvalidTypeValueException;
import lombok.Getter;

@Getter
public enum OAuthSocialType{
    NONE("none", -1),
    KAKAO("Kakao", 0),
    GOOGLE("Google", 1),
    NAVER("Naver", 2);

    private final String pascalName;
    private final int index;
    OAuthSocialType(String pascalName, int index){
        this.pascalName = pascalName;
        this.index = index;
    }

    public static OAuthSocialType fromValue(String socialName) {
        for (OAuthSocialType oAuthSocialType : OAuthSocialType.values()) {
            if (oAuthSocialType.name().equals(socialName))
                return oAuthSocialType;
        }
        throw new InvalidTypeValueException();
    }
    public static OAuthSocialType fromValue(int index) {
        for (OAuthSocialType oAuthSocialType : OAuthSocialType.values()) {
            if (oAuthSocialType.getIndex() == index)
                return oAuthSocialType;
        }
        throw new InvalidTypeValueException();
    }

}
