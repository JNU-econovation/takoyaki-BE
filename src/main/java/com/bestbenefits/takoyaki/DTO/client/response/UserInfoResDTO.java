package com.bestbenefits.takoyaki.DTO.client.response;


import com.bestbenefits.takoyaki.config.properties.auth.OAuthSocialType;
import lombok.Builder;

@Builder
public record UserInfoResDTO(String nickname, OAuthSocialType social, String email) {}
