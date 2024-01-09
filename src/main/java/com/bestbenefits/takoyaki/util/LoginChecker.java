package com.bestbenefits.takoyaki.util;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import jakarta.servlet.http.HttpSession;

public class LoginChecker {
    public static boolean isLogin(HttpSession session) {
        return isLogin((Long) session.getAttribute(SessionConst.ID), (Boolean) session.getAttribute(SessionConst.AUTHENTICATION));
    }

    public static boolean isLogin(Long id, Boolean authentication) {
        return id != null && authentication != null && authentication;
    }
}
