package com.bestbenefits.takoyaki.DTO.client.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PartyYakiListResDTO {
    private Long id;
    private String nickname;

    @Builder
    public PartyYakiListResDTO(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
