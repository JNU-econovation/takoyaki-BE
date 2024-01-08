package com.bestbenefits.takoyaki.DTO.client.request;

import com.bestbenefits.takoyaki.config.properties.party.PartyListTypeEnum;
import lombok.Getter;

@Getter
public class PartyListReqDTO {
    private PartyListTypeEnum partyListType;
    private Boolean loginField;
    private Integer number;
    private Integer pageNumber;
    private String categoryName;
    private String activityLocationName;

    public PartyListReqDTO(String type, Boolean login, Integer number, Integer page_number, String category, String activity_location) {
        this.partyListType = PartyListTypeEnum.fromValue(type);
        if (type.equalsIgnoreCase(PartyListTypeEnum.ALL.name())){
            if (login == null || number == null || page_number == null)
                throw new IllegalArgumentException("all일 경우에 필요한 인자를 확인해주세요.");
            this.loginField = login;
            this.number = number;
            this.pageNumber = page_number;
            this.categoryName = category;
            this.activityLocationName = activity_location;
        }
    }
}
