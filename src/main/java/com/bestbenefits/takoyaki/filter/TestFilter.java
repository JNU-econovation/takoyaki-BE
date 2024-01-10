package com.bestbenefits.takoyaki.filter;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class TestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 코드
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("========================================================");

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Cookie[] cookies = httpServletRequest.getCookies();
        System.out.println("getSession().getAttribute(SessionConst.ID) = " + ((HttpServletRequest) request).getSession().getAttribute(SessionConst.ID));
        System.out.println("getSession().getAttribute(SessionConst.AUTHENTICATION) = " + ((HttpServletRequest) request).getSession().getAttribute(SessionConst.AUTHENTICATION));
        System.out.println("isRequestedSessionIdValid() = " + ((HttpServletRequest) request).isRequestedSessionIdValid());
        System.out.println("httpServletRequest.getSession(false).isNew() = " + httpServletRequest.getSession(false).isNew());
        for(Cookie cookie : cookies){
            System.out.println("cookie.getName() = " + cookie.getName());
            System.out.println("cookie.getValue() = " + cookie.getValue());
            System.out.println("cookie.getMaxAge() = " + cookie.getMaxAge());
        }

        // 요청 전/후 처리 로직을 작성
         chain.doFilter(request, response);
    }


}