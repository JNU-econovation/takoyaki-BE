package com.bestbenefits.takoyaki.DTO.client.request;

import lombok.Getter;
import org.springframework.beans.BeanInstantiationException;

@Getter
public class PartyListReqDTO {
    private final Integer number;
    private final Integer pageNumber;
    private final String categoryName;
    private final String activityLocationName;

    public PartyListReqDTO(Integer number, Integer page_number, String category, String activity_location) {
        if (number == null || page_number == null)
            throw new BeanInstantiationException(PartyListReqDTO.class, "number, page_number은 null일 수 없음");

        this.number = number;
        this.pageNumber = page_number;
        this.categoryName = category;
        this.activityLocationName = activity_location;
    }
}
