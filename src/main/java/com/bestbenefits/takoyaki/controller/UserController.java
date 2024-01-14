package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.UserNicknameUpdateReqDTO;
import com.bestbenefits.takoyaki.config.annotation.DontCareAuthentication;
import com.bestbenefits.takoyaki.config.annotation.NeedAuthentication;
import com.bestbenefits.takoyaki.config.annotation.Session;
import com.bestbenefits.takoyaki.config.apiresponse.*;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.service.UserService;
import com.bestbenefits.takoyaki.util.LoginChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final LoginChecker loginChecker;

    @DontCareAuthentication
    @GetMapping("/login-check")
    public ApiResponse<?> checkLogin(HttpServletRequest request){
        //TODO: 변경된 로직이 기존과 다른 점이 없는지 다시 확인하기
        Map<String, Boolean> data = new HashMap<>();
        data.put("login", loginChecker.isLogin(request));
        return ApiResponseCreator.success(data);
    }

    @DontCareAuthentication
    @GetMapping("/duplicate-nickname")
    public ApiResponse<?> checkDuplicateNickname(@RequestParam String nickname){

        Map<String, Boolean> data = new HashMap<>();
        data.put("duplicate-nickname", userService.checkDuplicateNickname(nickname));

        return ApiResponseCreator.success(data);
    }

    @NeedAuthentication
    @PatchMapping("/nickname")
    public ApiResponse<?> changeNickname(@Session(attribute = SessionConst.ID) Long id,
                                         @RequestBody @Valid UserNicknameUpdateReqDTO userNicknameUpdateReqDTO){
        userService.changeNickname(id, userNicknameUpdateReqDTO);
        return ApiResponseCreator.success();
    }

    @NeedAuthentication
    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConst.ID); //로그아웃하면 세션 attribute 다 날리기
        session.removeAttribute(SessionConst.AUTHENTICATION);
        session.invalidate();
        return ApiResponseCreator.success();
    }

    @NeedAuthentication
    @GetMapping("/info")
    public ApiResponse<?> getInfo(@Session(attribute = SessionConst.ID) Long id){
        return ApiResponseCreator.success(userService.getUserInfo(id));
    }
}
