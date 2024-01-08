package com.bestbenefits.takoyaki.config.properties.party;

public enum PartyListTypeEnum {
    ALL,
    WAITING,
    ACCEPTED,
    WROTE;
    public static PartyListTypeEnum fromValue(String typeName) {
        for (PartyListTypeEnum partyListTypeEnum : PartyListTypeEnum.values()) {
            if (partyListTypeEnum.name().equalsIgnoreCase(typeName))
                return partyListTypeEnum;
        }
        throw new IllegalArgumentException("Invalid type value.");
    }
}
