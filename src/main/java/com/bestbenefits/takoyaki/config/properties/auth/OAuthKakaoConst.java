package com.bestbenefits.takoyaki.config.properties.auth;

public final class OAuthKakaoConst {
    public static final String URL1 = "https://kauth.kakao.com";
    public static final String URL2 = "https://kapi.kakao.com";
    public static final String AUTHORIZATION_CODE_URI = "/oauth/authorize";
    public static final String ACCESS_TOKEN_URI = "/oauth/token";
    public static final String REDIRECT_URI = "https://api.takoyaki.store/oauth";
    public static final String AGREEMENT_URI = "/v2/user/scopes";
    public static final String USER_INFO_URI = "/v2/user/me";
    public static final String LOGOUT_URI = "/v1/user/logout";
    public static final String CLIENT_ID = "832c674221e0bd9d5f7a07f5c3ba5d85";

    private OAuthKakaoConst(){}
}
