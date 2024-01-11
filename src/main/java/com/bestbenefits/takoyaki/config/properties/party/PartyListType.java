package com.bestbenefits.takoyaki.config.properties.party;

import com.bestbenefits.takoyaki.exception.common.InvalidTypeValueException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PartyListType {
    ALL, //모든 팟
    NOT_CLOSED_WAITING, //마감되지 않은 대기 팟
    NOT_CLOSED_ACCEPTED, //마감되지 않은 수락 팟
    CLOSED, //마감된 수락 팟
    WROTE, //작성한 팟
    BOOKMARKED; //북마크 팟

    public static PartyListType fromName(String typeName) {
        for (PartyListType partyListType : PartyListType.values()) {
            if (partyListType.name().equalsIgnoreCase(typeName))
                return partyListType;
        }
        throw new InvalidTypeValueException("party_list_type", typeName);
    }

    public static List<String> toNameList(){
        return Arrays.stream(PartyListType.values())
                .map(PartyListType::name)
                .collect(Collectors.toList());
    }

}
