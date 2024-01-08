package com.bestbenefits.takoyaki.DTO.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyListResDTO {
    private Long partyId;
    private String title;
    private String category;
    private String activityLocation;
    private int recruitNumber;
    private LocalDate plannedClosingDate;
    private int waitingNumber;
    private int acceptedNumber;
    private float competitionRate;
    private Boolean bookmarked;

    @Builder
    public PartyListResDTO(Long partyId, String title, String category, int waitingNumber, int acceptedNumber, int recruitNumber, float competitionRate, String activityLocation, LocalDate plannedClosingDate, Boolean bookmarked) {
        this.partyId = partyId;
        this.title = title;
        this.category = category;
        this.waitingNumber = waitingNumber;
        this.acceptedNumber = acceptedNumber;
        this.recruitNumber = recruitNumber;
        this.competitionRate = competitionRate;
        this.activityLocation = activityLocation;
        this.plannedClosingDate = plannedClosingDate;
        this.bookmarked = bookmarked;
    }
}