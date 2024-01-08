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
                        "/parties/all"
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SessionMethodArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*");
    }

}
