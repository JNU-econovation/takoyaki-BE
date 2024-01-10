package com.bestbenefits.takoyaki.interceptor;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.exception.user.UnauthorizedException;
import com.bestbenefits.takoyaki.util.LoginChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AuthenticationCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();

        System.out.println("=============== Interceptor 작동 ===============");
        System.out.println("httpMethod = " + httpMethod);
        System.out.println("requestURI = " + requestURI);

        if (requestURI.matches("/users/oauth/login/additional-info")) {
            System.out.println("해당 URI는 커스텀 로그인 필요하므로 인터셉터 종료");
            return true; //커스텀 로그인 확인
        }
        if (requestURI.matches("/parties/\\d+") && httpMethod.equals("GET")) {
            System.out.println("해당 URI는 로그인 필요 없음");
            return true; //로그인 필요 없음
        }
        if (requestURI.matches("/parties/\\d+/comment") && httpMethod.equals("GET")) {
            System.out.println("해당 URI는 로그인 필요 없음");
            return true; //로그인 필요 없음
        }

        HttpSession session = request.getSession(false);

        if (!LoginChecker.isLogin(session)) {
            System.out.println("session = " + session);
            if (session != null) {
                System.out.println("id = " + (Long) session.getAttribute(SessionConst.ID));
                System.out.println("auth = " + (Boolean) session.getAttribute(SessionConst.AUTHENTICATION));
            }
            System.out.println(">>>>> UnauthorizedException in interceptor");
            throw new UnauthorizedException();
        }

        return true;
    }
}