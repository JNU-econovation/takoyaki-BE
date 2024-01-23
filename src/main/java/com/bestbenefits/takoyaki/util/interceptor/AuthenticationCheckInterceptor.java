package com.bestbenefits.takoyaki.util.interceptor;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.exception.user.UnauthorizedException;
import com.bestbenefits.takoyaki.util.LoginChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationCheckInterceptor implements HandlerInterceptor {
    private final LoginChecker loginChecker;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        try {
            log.info("====>>>> Execute Authentcation Check Interceptor");
            String httpMethod = request.getMethod();
            String requestURI = request.getRequestURI();

            log.info("httpMethod = {}, requestURI = {}", httpMethod, requestURI);

            //포트만 달라도 post options로 보내더라.
            //options는 세션이 담기지 않음. 이때 options가 로그인 Interceptor로 도달하면 오류가 남(
            if (httpMethod.equals("OPTIONS"))
                return true;

            if (requestURI.matches("/users/oauth/login/additional-info")) {
                return true;
            }
            if (requestURI.matches("/parties/\\d+") && httpMethod.equals("GET")) {
                log.info("해당 URI는 로그인 필요 없음");
                return true; //로그인 필요 없음
            }
            if (requestURI.matches("/parties/\\d+/comment") && httpMethod.equals("GET")) {
                log.info("해당 URI는 로그인 필요 없음");
                return true; //로그인 필요 없음
            }

            HttpSession session = request.getSession(false);

            if (!loginChecker.isLogin(session)) {
                log.info("session = {}", session);
                if (session != null) {
                    log.info("id = {}, auth = {}",
                            session.getAttribute(SessionConst.ID),
                            session.getAttribute(SessionConst.AUTHENTICATION));
                }
                throw new UnauthorizedException("Authentication Check Interceptor");
            }

            return true;
        } finally {
            log.info("<<<<==== Terminate Authentcation Check Interceptor");
        }
    }
}