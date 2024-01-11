package com.bestbenefits.takoyaki.interceptor;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.exception.user.UnauthorizedException;
import com.bestbenefits.takoyaki.util.LoginChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationCheckInterceptor implements HandlerInterceptor {
    private final LoginChecker loginChecker;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();

        System.out.println("=============== Interceptor 작동 ===============");
        System.out.println("httpMethod = " + httpMethod);
        System.out.println("requestURI = " + requestURI);


        //포트만 달라도 post options로 보내더라.
        //options는 세션이 담기지 않음. 이때 options가 로그인 Interceptor로 도달하면 오류가 남(
        if (httpMethod.equals("OPTIONS"))
            return true;

        if (requestURI.matches("/users/oauth/login/additional-info")) {
            return true;
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

        if (!loginChecker.isLogin(session)) {
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