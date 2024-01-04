package com.bestbenefits.takoyaki.config.properties.party;

import com.bestbenefits.takoyaki.config.properties.oauth.OAuthSocialType;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum ActivityLocation {
    NATIONWIDE("전국"),
    ONLINE("온라인"),
    METROPOLITAN_AREA("수도권"),
    GANGWON("강원"),
    CHUNGCHEONG("충청"),
    JEOLLA("전라"),
    GYEONGSANG("경상"),
    JEJU("제주");

    private final String name;

    ActivityLocation(String name){
        this.name = name;
    }

    public static ActivityLocation fromName(String activityLocationName) {
        for (ActivityLocation activityLocation : ActivityLocation.values()) {
            if (activityLocation.getName().equals(activityLocationName))
                return activityLocation;
        }
        throw new IllegalArgumentException("Invalid activity location name.");
    }

    public static List<String> toNameList(){
        return Arrays.stream(ActivityLocation.values())
                .map(ActivityLocation::getName)
                .collect(Collectors.toList());
    }
}