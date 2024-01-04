package com.bestbenefits.takoyaki.config.properties.party;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum DurationUnit {
    일(1), 주(7), 개월(30), 년(365);

    private final int day;
    DurationUnit(int day){
        this.day = day;
    }
    public static DurationUnit fromValue(String durationUnitName) {
        for (DurationUnit durationUnit : DurationUnit.values()) {
            if (durationUnit.name().equals(durationUnitName))
                return durationUnit;
        }
        throw new IllegalArgumentException("Invalid duration unit name.");
    }
}
