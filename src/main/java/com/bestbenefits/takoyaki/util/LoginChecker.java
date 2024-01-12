package com.bestbenefits.takoyaki.util;

import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class LoginChecker {
    private final UserRepository userRepository;
    public boolean isLogin(HttpServletRequest request) {
        return isLogin(request.getSession(false));
    }

    public boolean isLogin(HttpSession session) {
        return session != null && isLogin((Long) session.getAttribute(SessionConst.ID), (Boolean) session.getAttribute(SessionConst.AUTHENTICATION));
    }

    @Transactional
    public boolean isLogin(Long id, Boolean authentication) {
        return id != null && authentication != null && authentication && userRepository.findById(id).isPresent();
    }

    public boolean isLogout(Long id, Boolean authentication) {
        return id == null || (authentication == null || !authentication);
    }
}
