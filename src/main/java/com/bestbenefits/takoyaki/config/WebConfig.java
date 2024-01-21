package com.bestbenefits.takoyaki.config;

import com.bestbenefits.takoyaki.config.annotation.SessionMethodArgumentResolver;
import com.bestbenefits.takoyaki.interceptor.AuthenticationCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    //TODO: 종준형에게 물어보기
    private final AuthenticationCheckInterceptor authenticationCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authenticationCheckInterceptor)
                .addPathPatterns(
                        "/test/party/post-random",

                        "/users/oauth/login/additional-info", //커스텀 처리
                        "/users/logout",
                        "/users/nickname",
                        "/users/info",

                        "/parties/not-closed-accepted",
                        "/parties/not-closed-waiting",
                        "/parties/closed",
                        "/parties/wrote",
                        "/parties/bookmarked",

                        "/parties/{party-id:\\d+}", //GET 제외
                        "/parties/{party-id:\\d+}/closing",
                        "/parties/{party-id:\\d+}/apply",
                        "/parties/{party-id:\\d+}/applicant/{user-id:\\d+}",
                        "/parties/{party-id:\\d+}/leaving",
                        "/parties/{party-id:\\d+}/comment", //GET 제외
                        "/parties/{party-id:\\d+}/bookmark",

                        "/party"
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SessionMethodArgumentResolver());
    }



    //TODO: CORS 범위 지정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://3.34.197.43:3000",
                        "http://localhost:3000",
                        "http://3.34.197.43:8080",
                        "http://localhost:8080",
                        "https://takoyaki.store",
                        "https://www.takoyaki.store",
                        "https://d33m8jh2u1lel2.cloudfront.net"
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }



}
