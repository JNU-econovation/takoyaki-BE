package com.bestbenefits.takoyaki.config.annotation;

import com.bestbenefits.takoyaki.exception.user.UnauthorizedException;
import com.bestbenefits.takoyaki.util.LoginChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

public class SessionMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Session.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        boolean nullable = parameter.getParameterAnnotation(Session.class).nullable();
        String attribute = parameter.getParameterAnnotation(Session.class).attribute();

        if (session == null) {
            if (nullable) return null;
            else {
                //return null;
                throw new UnauthorizedException("Session Argument Resolver");
            }
        } else
            return session.getAttribute(attribute);

        //TODO: 로그인 안되어있어도 세션 예외가 발생하지 않도록 임시 조치하기
    }
}
