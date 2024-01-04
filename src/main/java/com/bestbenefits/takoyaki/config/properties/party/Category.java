package com.bestbenefits.takoyaki.config.properties.party;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Category {
    EXTERNAL_ACTIVITIES("대외활동"),
    JOB_STUDY("취업/스터디"),
    OTT("OTT"),
    HABIT_CHALLENGE("습관 챌린지"),
    GAME("게임"),
    SMALL_GROUP("소모임"),
    ETC("기타");

    private final String name;

    Category(String name){
        this.name = name;
    }

    public static Category fromName(String categoryName) {
        for (Category category : Category.values()) {
            if (category.getName().equals(categoryName))
                return category;
        }
        throw new IllegalArgumentException("Invalid category name.");
    }
    public static List<String> toNameList(){
        return Arrays.stream(Category.values())
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}
