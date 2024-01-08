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
                .addPathPatterns("/test/**", "/users/**", "/parties/**", "/party/**")

                //로그인 필요없는 URI들
                .excludePathPatterns(
                        //실험용이니 나중에 삭제 필요
                        "/js/**", "/oauth_example", "/oauth", "/favicon.ico",

                        "/test/users/signup",
                        "/test/users/login/**",
                        "/test/party/get-random",

                        "/users/login-check",
                        "/users/oauth/login-url/**",
                        "/users/oauth/login/**",
                        "/users/oauth/login/additional-info",
                        "/users/duplicate-nickname",

                        "/parties/",
                        //"/parties/{party-id:\\d+}", //GET만 exclude 해야됨
                        //"/parties/{party-id:\\d+}/comment", //GET만 exclude 해야됨

                        "/party/category",
                        "/party/activity-location",
                        "/party/activity-duration-unit",
                        "/party/contact-method"
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
