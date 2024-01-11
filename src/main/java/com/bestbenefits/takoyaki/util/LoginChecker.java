package com.bestbenefits.takoyaki.util;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoginChecker {
    public boolean isLogin(HttpServletRequest request) {
        return isLogin(request.getSession(false));
    }

    public boolean isLogin(HttpSession session) {
        return session != null && isLogin((Long) session.getAttribute(SessionConst.ID), (Boolean) session.getAttribute(SessionConst.AUTHENTICATION));
    }

    public boolean isLogin(Long id, Boolean authentication) {
        return id != null && authentication != null && authentication;
    }

    public boolean isLogout(Long id, Boolean authentication) {
        return id == null || (authentication == null || !authentication);
    }
}
