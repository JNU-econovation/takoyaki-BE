package com.bestbenefits.takoyaki.DTO.client.request;

import com.bestbenefits.takoyaki.config.annotation.EnumName;
import com.bestbenefits.takoyaki.config.annotation.EnumValue;
import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.config.properties.party.ContactMethod;
import com.bestbenefits.takoyaki.config.properties.party.DurationUnit;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;


/*
{
    "category": "동아리",
    "activity_location": "광주광역시",
    "contact_method": "카카오톡",

    "title": "에코노베이션 27기 모집",
    "body": "열정있는 27기를 모집합니다!",
    "recruit_number": 5, // or 문자열
    "activity_duration": "1개월",
    "planned_closing_date": "2024-03-15",
    "contact": "https://카카오톡URL.."
}
 */

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PartyCreationReqDTO {
    @EnumName(enumClass = Category.class)
    private String category;

    @EnumName(enumClass = ActivityLocation.class)
    private String activityLocation;

    @EnumName(enumClass = ContactMethod.class)
    private String contactMethod;

    @EnumValue(enumClass = DurationUnit.class)
    private String activityDurationUnit;

    //제목
    @NotBlank
    @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
    private String title;

    //본문
    @NotBlank
    private String body;

    //모집 인원 수
    @NotNull
    @Positive(message = "모집 인원수는 1명 이상이어야 합니다.")
    private Integer recruitNumber;

    //활동 기간
    @NotNull
    @Positive(message = "활동 기간은 1 이상이어야 합니다.")
    private Integer activityDuration;

    //마감 예정 일시
    @Future(message = "마감 예정 일시는 미래여야 합니다.")
    private LocalDate plannedClosingDate;

    //연락처
    @NotBlank
    private String contact;

    public Party toEntity(User user){
        return Party.builder()
                .category(Category.fromName(category))
                .activityLocation(ActivityLocation.fromName(activityLocation))
                .contactMethod(ContactMethod.fromName(contactMethod))
                .title(title)
                .body(body)
                .recruitNumber(recruitNumber)
                .contact(contact)
                .plannedClosingDate(plannedClosingDate)
                .activityDuration(activityDuration * DurationUnit.fromValue(activityDurationUnit).getDay())
                .user(user)
                .build();
    }
}
