package com.bestbenefits.takoyaki.DTO.client.request;

import com.bestbenefits.takoyaki.config.annotation.Nickname;
import lombok.Getter;

@Getter
public class UserAdditionalInfoReqDTO {
    @Nickname
    private String nickname;
    //...
}
