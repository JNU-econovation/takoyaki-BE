package com.bestbenefits.takoyaki.config.properties.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuthKakaoURL implements OAuthURL{
    private final String loginURL = OAuthKakaoConst.URL1 +
                         UriComponentsBuilder.newInstance()
                        .path(OAuthKakaoConst.AUTHORIZATION_CODE_URI)
                        .queryParam("response_type", "code")
                        .queryParam("client_id", OAuthKakaoConst.CLIENT_ID)
                        .queryParam("redirect_uri", OAuthKakaoConst.REDIRECT_URI)
                        .queryParam("prompt", "select_account")
                        .build(true);
    @Override
    public String getLoginURL() {
        return loginURL;
    }
}
