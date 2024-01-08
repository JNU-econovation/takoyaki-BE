package com.bestbenefits.takoyaki.interceptor;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.exception.NotLoginException;
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
        //로그인 필요 없는 URI들
        if (requestURI.matches("/parties/\\d+") && httpMethod.equals("GET")) {
            return true;
        }
        if (requestURI.matches("/parties/\\d+/comment") && httpMethod.equals("GET")) {
            return true;
        }
        if (!isLogin(request.getSession()))
            throw new NotLoginException();
            //TODO: 커스텀 예외 작성 후(완료) 상태 코드 401(해야됨)

        return true;
    }

    public static boolean isLogin(HttpSession session) {
        return isLogin((Long) session.getAttribute(SessionConst.ID), (Boolean) session.getAttribute(SessionConst.AUTHENTICATION));
    }

    public static boolean isLogin(Long id, Boolean authentication) {
        return id != null && authentication != null && authentication;
    }
}