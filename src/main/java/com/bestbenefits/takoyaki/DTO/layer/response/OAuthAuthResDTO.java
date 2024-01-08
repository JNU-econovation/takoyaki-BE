package com.bestbenefits.takoyaki.DTO.layer.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAuthResDTO {
    private Long id;
    private boolean isInfoNeeded;

    @Builder
    OAuthAuthResDTO(Long id, boolean isInfoNeeded){
        this.id= id;
        this.isInfoNeeded = isInfoNeeded;
    }
}
