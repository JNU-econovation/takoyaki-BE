package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.DTO.client.request.UserAdditionalInfoReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.UserNicknameUpdateReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.UserInfoResDTO;
import com.bestbenefits.takoyaki.DTO.layer.request.OAuthSignUpReqDTO;
import com.bestbenefits.takoyaki.DTO.layer.response.OAuthAuthResDTO;
import com.bestbenefits.takoyaki.config.properties.auth.OAuthSocialType;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.exception.user.*;
import com.bestbenefits.takoyaki.repository.UserRepository;
import com.bestbenefits.takoyaki.util.LoginChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LoginChecker loginChecker;

    @Transactional(readOnly = true)
    public User tempLogin(Long id){
        return getUserOrThrow(id);
    }

    @Transactional
    public User tempSignUp(){
        Random random = new Random();
        User user = userRepository.save(User.builder()
                .email("temp_email"+ (random.nextInt(990000) + 9999) +"@temp.com")
                .social(OAuthSocialType.NONE)
                .build());
        user.updateNickname("임시닉네임"+user.getId());
        return user;
    }

    @Transactional(readOnly = true)
    public boolean checkDuplicateNickname(String nickname){
        return userRepository.findUserByNickname(nickname).isPresent();
    }

    @Transactional(readOnly = true)
    public OAuthAuthResDTO loginByOAuth(String email, OAuthSocialType socialType){
        User user = userRepository.findUserByEmailAndSocial(email, socialType).orElse(null);
        if (user != null)
            return OAuthAuthResDTO.builder()
                    .id(user.getId())
                    .isInfoNeeded(user.getNickname() == null)
                    .build();
        else
            return null;
    }

    public OAuthAuthResDTO signUpByOAuth(OAuthSignUpReqDTO oAuthSignUpReqDTO){
        User user = userRepository.save(oAuthSignUpReqDTO.toEntity());
        return OAuthAuthResDTO.builder()
                .id(user.getId())
                .isInfoNeeded(true)
                .build();
    }

    @Transactional
    public void insertAdditionalInfo(Long id, Boolean authentication, UserAdditionalInfoReqDTO userAdditionalInfoReqDTO){
        if (loginChecker.isLogout(id, authentication)) {

            System.out.println(">>>>> UnauthorizedException in UserService");
            throw new UnauthorizedException();
        }

        User user = getUserOrThrow(id);
        if (user.getNickname() != null)
            throw new AdditionalInfoProvidedException();

        String nickname = userAdditionalInfoReqDTO.getNickname();
        if(checkDuplicateNickname(nickname))
            throw new DuplicateNicknameException();

        user.updateNickname(nickname);
        //another info...
    }

    @Transactional
    public void changeNickname(Long id, UserNicknameUpdateReqDTO userNicknameUpdateReqDTO){
        User user = getUserOrThrow(id);

        if (user.getNicknameUpdatedAt().isEqual(LocalDate.now()))
            throw new NicknameChangeTooEarlyException();

        if (checkDuplicateNickname(userNicknameUpdateReqDTO.getNickname()))
            throw new DuplicateNicknameException();

        user.updateNickname(userNicknameUpdateReqDTO.getNickname());
        user.updateNicknameUpdatedAt();
    }

    @Transactional(readOnly = true)
    public UserInfoResDTO getUserInfo(Long id){
        User user = getUserOrThrow(id);
        return UserInfoResDTO.builder()
                .nickname(user.getNickname())
                .social(user.getSocial())
                .email(user.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    public User getUserOrNull(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public User getUserOrThrow(Long id) {
        return userRepository.findUserById(id).orElseThrow(UserNotFoundException::new);
    }
}