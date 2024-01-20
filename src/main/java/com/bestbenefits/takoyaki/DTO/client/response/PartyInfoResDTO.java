package com.bestbenefits.takoyaki.DTO.client.response;

import com.bestbenefits.takoyaki.config.properties.user.UserType;
import com.bestbenefits.takoyaki.config.properties.user.YakiStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyInfoResDTO {
    //기본 값
    private String category;
    private String activityLocation;
    private String contactMethod;
    private String title;
    private String body;
    private int viewCount;
    private int recruitNumber;
    private String activityDuration;
    private LocalDate plannedStartDate;
    private LocalDate plannedClosingDate;
    //제거된 키값
    //private String activityDurationUnit;

    //추가된 키값
    private UserType userType; //사용자 타입 (meta로 제공)
    private Long partyId;
    private String nickname;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private LocalDate closedDate;

    //타코
    private List<PartyYakiListResDTO> waitingList;
    private List<PartyYakiListResDTO> acceptedList;
    //야끼
    private YakiStatus yakiStatus;
    //수락됨 & 마감된 야끼 or 타코
    private String contact;

    public UserType moveUserType() {
        UserType ret = this.userType;
        this.userType = null;
        return ret;
    }
}