package com.bestbenefits.takoyaki.config;

import com.bestbenefits.takoyaki.config.annotation.SessionMethodArgumentResolver;
import com.bestbenefits.takoyaki.interceptor.AuthenticationCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationCheckInterceptor())
                .addPathPatterns(
                        "/test/party/post-random",

                        "/users/oauth/login/additional-info", //커스텀 처리
                        "/users/logout",
                        "/users/nickname",
                        "/users/info",

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
                .allowedOrigins("http://localhost:8080") //withCredential이 true일 경우 와일드카드 안됨, 사용자가 접속한 url 기준(프론트 서버가 로컬이면 localhost)
//                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
