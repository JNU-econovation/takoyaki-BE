package com.bestbenefits.takoyaki.DTO.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyListResDTO {
    //기본 제공(12개)
    private final Long partyId;
    private final String title;
    private final String category;
    private final String activityLocation;
    private final Long viewCount;
    private final int waitingNumber;
    private final int acceptedNumber;
    private final String occupationRate; // 수락인원/모집인원
    private final int recruitNumber;
    private final String plannedClosingDate;
    private final String competitionRate;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private final LocalDate closedDate;

    //로그인 시 제공
    private final Boolean bookmarked;

}