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
    private final Long partyId;
    private final String title;
    private final String category;
    private final String activityLocation;
    private final int recruitNumber;
    private final LocalDate plannedClosingDate;
    private final int waitingNumber;
    private final int acceptedNumber;
    private final float competitionRate;
    private final Boolean bookmarked;
    private final Boolean closed;

    @Builder
    public PartyListResDTO(Long partyId, String title, String category, int waitingNumber, int acceptedNumber, int recruitNumber, float competitionRate, String activityLocation, LocalDate plannedClosingDate, Boolean bookmarked, Boolean closed) {
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
        this.closed = closed;
    }
}