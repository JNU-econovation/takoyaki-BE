package com.bestbenefits.takoyaki.util;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LoginChecker {
    public static boolean isLogin(HttpServletRequest request) {
        return isLogin(request.getSession(false));
    }

    public static boolean isLogin(HttpSession session) {
        return session != null && isLogin((Long) session.getAttribute(SessionConst.ID), (Boolean) session.getAttribute(SessionConst.AUTHENTICATION));
    }

    public static boolean isLogin(Long id, Boolean authentication) {
        return id != null && authentication != null && authentication;
    }

    public static boolean isLogout(Long id, Boolean authentication) {
        return id == null || (authentication == null || !authentication);
    }
}
