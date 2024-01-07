package com.bestbenefits.takoyaki.interceptor;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();

        //TODO: 여러 요청 추가해도 깔끔하도록 리팩토링
        if (requestURI.matches("/parties/\\d+") && httpMethod.equals("GET")) {
            return true;
        }

        HttpSession session = request.getSession();
        Boolean authentication = (Boolean) session.getAttribute(SessionConst.AUTHENTICATION);

        if (authentication == null || !authentication)
            throw new IllegalStateException("you need login.");
            //TODO: 커스텀 예외 작성 후 상태 코드 401
        return true;
    }
}