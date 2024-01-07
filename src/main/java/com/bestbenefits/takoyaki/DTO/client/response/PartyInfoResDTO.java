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
    private Long partyId;
    private String title;
    private String nickname;
    private String body;
    private String category;
    private String activityLocation;
    private String contactMethod;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private LocalDate closedDate;
    private LocalDate plannedStartDate;
    private String activityDuration;
    private LocalDate plannedClosingDate;
    private int recruitNumber;
    private int viewCount;

    private UserType userType; //로그인 유저
    //타코
    private List<PartyYakiListResDTO> waitingList;
    private List<PartyYakiListResDTO> acceptedList;
    //야끼
    private YakiStatus yakiStatus;
    private String contact; //수락, 마감

    @Builder
    public PartyInfoResDTO(Long partyId, String title, String nickname, String body, String category, String activityLocation, String contactMethod, LocalDate closedDate, LocalDate plannedStartDate, String activityDuration, LocalDate plannedClosingDate, int recruitNumber, int viewCount, UserType userType, List<PartyYakiListResDTO> waitingList, List<PartyYakiListResDTO> acceptedList, YakiStatus yakiStatus, String contact) {
        this.partyId = partyId;
        this.title = title;
        this.nickname = nickname;
        this.body = body;
        this.category = category;
        this.activityLocation = activityLocation;
        this.contactMethod = contactMethod;
        this.closedDate = closedDate;
        this.plannedStartDate = plannedStartDate;
        this.activityDuration = activityDuration;
        this.plannedClosingDate = plannedClosingDate;
        this.recruitNumber = recruitNumber;
        this.viewCount = viewCount;
        this.userType = userType;
        this.waitingList = waitingList;
        this.acceptedList = acceptedList;
        this.yakiStatus = yakiStatus;
        this.contact = contact;
    }
}