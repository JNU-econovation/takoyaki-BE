package com.bestbenefits.takoyaki.DTO.client.request;

import com.bestbenefits.takoyaki.config.properties.party.PartyListType;
import lombok.Getter;

@Getter
public class PartyListReqDTO {
    private PartyListType partyListType;
    private Integer number;
    private Integer pageNumber;
    private String categoryName;
    private String activityLocationName;

    public PartyListReqDTO(String type, Integer number, Integer page_number, String category, String activity_location) {
        this.partyListType = PartyListType.fromName(type);
        if (type.equalsIgnoreCase(PartyListType.ALL.name())){
            if (number == null || page_number == null)
                throw new IllegalArgumentException("all일 경우에 필요한 인자를 확인해주세요.");
            this.number = number;
            this.pageNumber = page_number;
            this.categoryName = category;
            this.activityLocationName = activity_location;
        }
    }
}
