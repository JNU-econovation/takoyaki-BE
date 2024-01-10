package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.UserAdditionalInfoReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.UserNicknameUpdateReqDTO;
import com.bestbenefits.takoyaki.DTO.layer.request.OAuthSignUpReqDTO;
import com.bestbenefits.takoyaki.DTO.layer.response.OAuthAuthResDTO;
import com.bestbenefits.takoyaki.DTO.server.response.TokensResDTO;
import com.bestbenefits.takoyaki.DTO.server.response.SocialUserInfoResDTO;
import com.bestbenefits.takoyaki.config.annotation.DontCareAuthentication;
import com.bestbenefits.takoyaki.config.annotation.NeedAuthentication;
import com.bestbenefits.takoyaki.config.annotation.NeedNoAuthentication;
import com.bestbenefits.takoyaki.config.annotation.Session;
import com.bestbenefits.takoyaki.config.apiresponse.*;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.config.properties.auth.OAuthSocialType;
import com.bestbenefits.takoyaki.config.properties.auth.OAuthURL;
import com.bestbenefits.takoyaki.exception.user.LogoutRequiredException;
import com.bestbenefits.takoyaki.interceptor.AuthenticationCheckInterceptor;
import com.bestbenefits.takoyaki.service.UserService;
import com.bestbenefits.takoyaki.util.LoginChecker;
import com.bestbenefits.takoyaki.util.webclient.oauth.OAuthWebClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final Map<String, OAuthWebClient> oAuthWebClient;
    private final Map<String, OAuthURL> oAuthURL;

    @DontCareAuthentication
    @GetMapping("/login-check")
    public ResponseEntity<?> checkLogin(HttpServletRequest request){
        Map<String, Boolean> data = new HashMap<>();
        data.put("login", LoginChecker.isLogin(request));
        return ResponseEntityCreator.success(data);
    }

    @DontCareAuthentication
    @GetMapping("/oauth/login-url/{social}")
    public ResponseEntity<?> getOAuthLoginUrl(@PathVariable String social){
        OAuthSocialType oAuthSocialType = OAuthSocialType.fromValue(social.toUpperCase());
        OAuthURL oAuthSocialURL = oAuthURL.get("OAuth" + oAuthSocialType.getPascalName() + "URL");

        Map<String, String> data = new HashMap<>();
        data.put("login_url", oAuthSocialURL.getLoginURL());

        return ResponseEntityCreator.success(data);
    }

    @DontCareAuthentication
    @GetMapping("/duplicate-nickname")
    public ResponseEntity<?> checkDuplicateNickname(@RequestParam String nickname){

        Map<String, Boolean> data = new HashMap<>();
        data.put("duplicate-nickname", userService.checkDuplicateNickname(nickname));

        return ResponseEntityCreator.success(data);
    }

    //TODO: 여러 소셜 받기
    @NeedNoAuthentication
    @PostMapping("/oauth/login/{social}")
    public ResponseEntity<?> login(HttpServletRequest request, @PathVariable String social, @RequestParam String code) {
        if (LoginChecker.isLogin(request.getSession(false)))
            throw new LogoutRequiredException();

        //get social-type enum
        OAuthSocialType oAuthSocialType = OAuthSocialType.fromValue(social.toUpperCase());
        //소셜 플랫폼에 따라 OAuth 요청을 수행할 객체를 가져옴
        OAuthWebClient oAuthSocialWebClient = oAuthWebClient.get("OAuth"+ oAuthSocialType.getPascalName()+"WebClient");
        //request tokens to resource server by Authorization code
        TokensResDTO tokensResDTO = oAuthSocialWebClient.requestTokens(code);
        //request user's info to resource server by access token
        SocialUserInfoResDTO socialUserInfoResDTO = oAuthSocialWebClient.requestUserInfo(tokensResDTO.getAccessToken());
        //check whether this user exists in DataBase by using email & social type
        OAuthAuthResDTO oAuthAuthResDTO = userService.loginByOAuth(socialUserInfoResDTO.getEmail(), oAuthSocialType);

        HttpSession session = request.getSession();
        HttpStatus status;
        //등록된 유저고, 추가 정보 있으면 로그인 완료
        //세션에 ID, AUTHENTICATION 등록됨
        if (oAuthAuthResDTO != null && !oAuthAuthResDTO.isInfoNeeded()) {
            session.setAttribute(SessionConst.AUTHENTICATION, true);
            status = HttpStatus.OK;
        }
        //등록되지 않은 유저면 유저 등록 필요
        //세션에 ID만 등록됨
        else {
            oAuthAuthResDTO = userService.signUpByOAuth(OAuthSignUpReqDTO.builder()
                    .socialUserInfoResDTO(socialUserInfoResDTO)
                    .social(oAuthSocialType)
                    .build());
            status = HttpStatus.CREATED;
        }

        session.setAttribute(SessionConst.ID, oAuthAuthResDTO.getId());
        Map<String, Boolean> data = new HashMap<>();
        data.put("is_info_needed", oAuthAuthResDTO.isInfoNeeded());

        return ResponseEntityCreator.success(data, status);
    }

    @NeedAuthentication
    @PostMapping("/oauth/login/additional-info")
    public ResponseEntity<?> signup(HttpServletRequest request,
                                    @Session(attribute = SessionConst.ID, nullable = true) Long id,
                                    @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
                                    @RequestBody @Valid UserAdditionalInfoReqDTO userAdditionalInfoReqDTO) {
        userService.insertAdditionalInfo(id, authentication, userAdditionalInfoReqDTO);
        request.getSession(false).setAttribute(SessionConst.AUTHENTICATION, true);
        return ResponseEntityCreator.success(HttpStatus.CREATED);
    }

    @NeedAuthentication
    @PatchMapping("/nickname")
    public ResponseEntity<?> changeNickname(@Session(attribute = SessionConst.ID) Long id,
                                                     @RequestBody @Valid UserNicknameUpdateReqDTO userNicknameUpdateReqDTO){
        userService.changeNickname(id, userNicknameUpdateReqDTO);
        return ResponseEntityCreator.success();
    }

    @NeedAuthentication
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConst.ID); //로그아웃하면 세션 attribute 다 날리기
        session.removeAttribute(SessionConst.AUTHENTICATION);
        session.invalidate();
        return ResponseEntityCreator.success();
    }

    @NeedAuthentication
    @GetMapping("/info")
    public ResponseEntity<?> getInfo(@Session(attribute = SessionConst.ID) Long id){
        return ResponseEntityCreator.success(userService.getUserInfo(id));
    }
}
