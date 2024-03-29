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
                .addPathPatterns("/users/**")
                .addPathPatterns("/parties/**")
                .addPathPatterns("/party/**") //TODO: 이거 /party로 요청하면 걸리게 되는거랑 연관성 있음
                .excludePathPatterns(
                        "/js/**", "/oauth_example", "/oauth", "/favicon.ico", "/users/temp/**", //TODO: 실험용이니 나중에 삭제하기

                        "/users/login-check",
                        "/users/oauth/login-url/**",
                        "/users/oauth/login/**",
                        "/users/duplicate-nickname",
                        "/users/oauth/login/additional-info",

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
                .allowedOrigins("http://3.34.197.43:3000", "http://localhost:3000", "http://3.34.197.43:8080", "http://localhost:8080")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }



}
