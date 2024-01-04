package com.bestbenefits.takoyaki.config.properties.party;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum DurationUnit {
    DAY("일", 1), WEEK("주", 7), MONTH("개월", 30), YEAR("년", 365);

    private final String name;
    private final int day;
    DurationUnit(String name, int day){
        this.name = name;
        this.day = day;
    }
    public static DurationUnit fromName(String durationUnitName) {
        for (DurationUnit durationUnit : DurationUnit.values()) {
            if (durationUnit.getName().equals(durationUnitName))
                return durationUnit;
        }
        throw new IllegalArgumentException("Invalid duration unit name.");
    }
    public static List<String> toNameList(){
        return Arrays.stream(DurationUnit.values())
                .map(DurationUnit::getName)
                .collect(Collectors.toList());
    }
}
