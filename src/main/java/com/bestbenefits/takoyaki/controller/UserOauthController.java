package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.UserAdditionalInfoReqDTO;
import com.bestbenefits.takoyaki.DTO.layer.request.OAuthSignUpReqDTO;
import com.bestbenefits.takoyaki.DTO.layer.response.OAuthAuthResDTO;
import com.bestbenefits.takoyaki.DTO.server.response.SocialUserInfoResDTO;
import com.bestbenefits.takoyaki.DTO.server.response.TokensResDTO;
import com.bestbenefits.takoyaki.config.annotation.*;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.config.properties.auth.OAuthSocialType;
import com.bestbenefits.takoyaki.config.properties.auth.OAuthURL;
import com.bestbenefits.takoyaki.exception.user.LogoutRequiredException;
import com.bestbenefits.takoyaki.service.UserService;
import com.bestbenefits.takoyaki.util.LoginChecker;
import com.bestbenefits.takoyaki.util.StringModer;
import com.bestbenefits.takoyaki.util.webclient.oauth.OAuthWebClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/oauth")
public class UserOauthController {
    private final UserService userService;
    private final Map<String, OAuthWebClient> oAuthWebClient;
    private final Map<String, OAuthURL> oAuthURL;
    private final LoginChecker loginChecker;

    @DontCareAuthentication
    @GetMapping("login-url/{social}")
    public ApiResponse<?> getOAuthLoginUrl(@PathVariable String social){
        OAuthSocialType oAuthSocialType = OAuthSocialType.fromName(social);
        OAuthURL oAuthSocialURL = oAuthURL.get("OAuth" + StringModer.toPascal(oAuthSocialType.getName()) + "URL");

        Map<String, String> data = new HashMap<>();
        data.put("login_url", oAuthSocialURL.getLoginURL());

        return ApiResponseCreator.success(data);
    }

    //TODO: 여러 소셜 받기
    @NeedNoAuthentication
    @PostMapping("/login/{social}")
    public ApiResponse<?> login(HttpServletRequest request, @PathVariable String social, @RequestParam String code) {
/*        if (loginChecker.isLogin(request.getSession(false)))
            throw new LogoutRequiredException();*/

        //get social-type enum
        OAuthSocialType oAuthSocialType = OAuthSocialType.fromName(social);
        //소셜 플랫폼에 따라 OAuth 요청을 수행할 객체를 가져옴
        OAuthWebClient oAuthSocialWebClient = oAuthWebClient.get("OAuth" + StringModer.toPascal(oAuthSocialType.getName()) + "WebClient");
        //request tokens to resource server by Authorization code
        TokensResDTO tokensResDTO = oAuthSocialWebClient.requestTokens(code);
        //request user's info to resource server by access token
        SocialUserInfoResDTO socialUserInfoResDTO = oAuthSocialWebClient.requestUserInfo(tokensResDTO.getAccessToken());
        //check whether this user exists in DataBase by using email & social type
        OAuthAuthResDTO oAuthAuthResDTO = userService.loginByOAuth(socialUserInfoResDTO.getEmail(), oAuthSocialType);

        HttpSession session = request.getSession();
        HttpStatus status;

        //세션에 ID, AUTHENTICATION 등록됨
        if (oAuthAuthResDTO != null) { //등록된 유저고,
            if (!oAuthAuthResDTO.isInfoNeeded()) //추가 정보 있으면 로그인 완료
                session.setAttribute(SessionConst.AUTHENTICATION, true);
            status = HttpStatus.OK;
        }else {//등록되지 않은 유저면 유저 등록 필요 - 세션에 ID만 등록됨
            oAuthAuthResDTO = userService.signUpByOAuth(OAuthSignUpReqDTO.builder()
                    .socialUserInfoResDTO(socialUserInfoResDTO)
                    .social(oAuthSocialType)
                    .build());
            status = HttpStatus.CREATED;
        }

        session.setAttribute(SessionConst.ID, oAuthAuthResDTO.getId());
        Map<String, Boolean> data = new HashMap<>();
        data.put("is_info_needed", oAuthAuthResDTO.isInfoNeeded());

        return ApiResponseCreator.success(data, status);
    }

    @NeedAuthentication
    @PostMapping("/login/additional-info")
    public ApiResponse<?> signup(HttpServletRequest request,
                                 @Session(attribute = SessionConst.ID) Long id,
                                 @RequestBody @Valid UserAdditionalInfoReqDTO userAdditionalInfoReqDTO) {
        //TODO: 세션이 null인지 더 확인하기
        userService.insertAdditionalInfo(id, userAdditionalInfoReqDTO);
        request.getSession(false).setAttribute(SessionConst.AUTHENTICATION, true);
        return ApiResponseCreator.success(HttpStatus.CREATED);
    }
}
