package com.bestbenefits.takoyaki.config.properties.party;

import com.bestbenefits.takoyaki.exception.common.InvalidTypeValueException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
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
        throw new InvalidTypeValueException("activity_duration_unit", durationUnitName);
    }
    public static List<String> toNameList(){
        return Arrays.stream(DurationUnit.values())
                .map(DurationUnit::getName)
                .collect(Collectors.toList());
    }
    public static String calculateDuration(int days){
        return Arrays.stream(DurationUnit.values())
                .sorted(Collections.reverseOrder())
                .filter(unit -> days % unit.getDay() == 0)
                .findFirst()
                .map(unit -> (days / unit.getDay()) + unit.getName())
                .orElseThrow(() -> new IllegalStateException("유효한 날짜가 아닙니다."));
    }
}
