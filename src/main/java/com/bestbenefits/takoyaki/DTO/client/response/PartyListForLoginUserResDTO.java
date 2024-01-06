package com.bestbenefits.takoyaki.DTO.client.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PartyListForLoginUserResDTO extends PartyListResDTO{
    private boolean bookmarked;

    public PartyListForLoginUserResDTO(Long partyId, String title, String category, int waitingNumber, int acceptedNumber, int recruitNumber, float competitionRate, String activityLocation, LocalDate plannedClosingDate, boolean bookmarked) {
        super(partyId, title, category, waitingNumber, acceptedNumber, recruitNumber, competitionRate, activityLocation, plannedClosingDate);
        this.bookmarked = bookmarked;
    }
}