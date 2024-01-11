package com.bestbenefits.takoyaki.config.properties.auth;

import com.bestbenefits.takoyaki.exception.common.InvalidTypeValueException;
import lombok.Getter;

@Getter
public enum OAuthSocialType {
    NONE("none"),
    KAKAO("kakao"),
    GOOGLE("google"),
    NAVER("naver");

    private final String name;
    OAuthSocialType(String name){
        this.name = name;
    }

    public static OAuthSocialType fromName(String socialName) {
        for (OAuthSocialType oAuthSocialType : OAuthSocialType.values()) {
            if (oAuthSocialType.getName().equals(socialName))
                return oAuthSocialType;
        }
        throw new InvalidTypeValueException("OauthSocialType", socialName);
    }
}
