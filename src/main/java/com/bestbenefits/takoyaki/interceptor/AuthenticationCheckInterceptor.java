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

        HttpSession session = request.getSession();
        Boolean authentication = (Boolean) session.getAttribute(SessionConst.AUTHENTICATION);

        if (authentication == null || !authentication)
            throw new IllegalStateException("you need login.");
            //TODO: 커스텀 예외 작성 후 상태 코드 401
        return true;
    }
}