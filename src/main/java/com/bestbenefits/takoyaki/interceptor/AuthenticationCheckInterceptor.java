package com.bestbenefits.takoyaki.interceptor;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.exception.NeedLoginException;
import com.bestbenefits.takoyaki.util.LoginChecker;
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

        //로그인 필요 없는 URI들
        if (requestURI.matches("/parties/\\d+") && httpMethod.equals("GET")) {
            return true;
        }
        if (requestURI.matches("/parties/\\d+/comment") && httpMethod.equals("GET")) {
            return true;
        }

        if (!LoginChecker.isLogin(request.getSession()))
            throw new NeedLoginException();

        return true;
    }
}