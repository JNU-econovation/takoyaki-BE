package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.config.annotation.DontCareAuthentication;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.config.properties.party.ContactMethod;
import com.bestbenefits.takoyaki.config.properties.party.DurationUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/party")
public class AvailableListController {
    @DontCareAuthentication
    @GetMapping("/activity-location")
    public ApiResponse<?> getActivityLocation() {
        Map<String, Integer> meta = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        meta.put("count", ActivityLocation.toNameList().size());
        data.put("activity_location", ActivityLocation.toNameList());
        return ApiResponseCreator.success(meta, data);
    }

    @DontCareAuthentication
    @GetMapping("/category")
    public ApiResponse<?> getCategory() {
        Map<String, Integer> meta = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        meta.put("count", Category.toNameList().size());
        data.put("category", Category.toNameList());
        return ApiResponseCreator.success(meta, data);
    }

    @DontCareAuthentication
    @GetMapping("/contact-method")
    public ApiResponse<?> getContactMethod() {
        Map<String, Integer> meta = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        meta.put("count", ContactMethod.toNameList().size());
        data.put("contact_method", ContactMethod.toNameList());
        return ApiResponseCreator.success(meta, data);
    }

    @DontCareAuthentication
    @GetMapping("/activity-duration-unit")
    public ApiResponse<?> getActivityDurationUnit() {
        Map<String, Integer> meta = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        meta.put("count", DurationUnit.toNameList().size());
        data.put("activity_duration_unit", DurationUnit.toNameList());
        return ApiResponseCreator.success(meta, data);
    }
}
