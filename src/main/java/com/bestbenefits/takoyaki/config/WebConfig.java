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
//                .addPathPatterns("/parties/**")
//                .addPathPatterns("/party/**")
                .excludePathPatterns(
                        "/js/**", "/oauth_example", "/oauth", "/favicon.ico", "/users/temp/**", //실험용이니 나중에 삭제하기

                        "/users/login-check",
                        "/users/oauth/login-url/**",
                        "/users/oauth/login/**",
                        "/users/duplicate-nickname",
                        "/users/oauth/login/additional-info",

                        "/party/category",
                        "/party/activity-location",
                        "/party/activity-duration-unit",
                        "/party/contact-method",

                        "/parties/all"
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
                .allowedOrigins("http://localhost:3000", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")//, "OPTIONS"
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
