package com.bestbenefits.takoyaki.DTO.client.request;

import com.bestbenefits.takoyaki.config.annotation.Nickname;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserNicknameUpdateReqDTO {
    @Nickname
    private String nickname;
}
