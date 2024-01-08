package com.bestbenefits.takoyaki.config.properties.party;

public enum PartyListTypeEnum {
    ALL, //모든 팟
    NOT_CLOSED_WAITING, //마감되지 않은 대기 팟
    NOT_CLOSED_ACCEPTED, //마감되지 않은 수락 팟
    CLOSED, //마감된 수락 팟
    WROTE, //작성한 팟
    BOOKMARKED; //북마크 팟

    public static PartyListTypeEnum fromValue(String typeName) {
        for (PartyListTypeEnum partyListTypeEnum : PartyListTypeEnum.values()) {
            if (partyListTypeEnum.name().equalsIgnoreCase(typeName))
                return partyListTypeEnum;
        }
        throw new IllegalArgumentException("Invalid type value.");
    }
}
