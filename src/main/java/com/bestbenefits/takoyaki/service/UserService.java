package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.DTO.client.request.UserAdditionalInfoReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.UserNicknameUpdateReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.UserInfoResDTO;
import com.bestbenefits.takoyaki.DTO.layer.request.OAuthSignUpReqDTO;
import com.bestbenefits.takoyaki.DTO.layer.response.OAuthAuthResDTO;
import com.bestbenefits.takoyaki.config.properties.auth.OAuthSocialType;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
    public void insertAdditionalInfo(Long id, UserAdditionalInfoReqDTO userAdditionalInfoReqDTO){
        String nickname = userAdditionalInfoReqDTO.getNickname();
        User user = getUserOrThrow(id);
        if (user.getNickname() != null)
            throw new IllegalArgumentException("User already has additional information");
//        if(userRepository.findUserByNickname(nickname).isPresent())
        if(checkDuplicateNickname(nickname))
            throw new IllegalArgumentException("duplicate nickname");
        user.updateNickname(nickname);
        //another info...
    }

    @Transactional
    public void changeNickname(Long id, UserNicknameUpdateReqDTO userNicknameUpdateReqDTO){
        User user = getUserOrThrow(id);

        if (user.getNicknameUpdatedAt().isEqual(LocalDate.now()))
            throw new IllegalArgumentException("닉네임은 하루에 한 번만 바꿀 수 있습니다.");

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
        return userRepository.findUserById(id).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
    }


}