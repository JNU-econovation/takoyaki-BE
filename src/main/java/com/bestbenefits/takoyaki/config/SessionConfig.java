package com.bestbenefits.takoyaki.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None"); // SameSite 설정 ("None", "Lax", "Strict" 중 선택)
        serializer.setUseSecureCookie(true); // Secure 설정 (true 또는 false 중 선택)
        return serializer;
    }
}
