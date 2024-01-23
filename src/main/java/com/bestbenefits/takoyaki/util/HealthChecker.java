package com.bestbenefits.takoyaki.util;

import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//AWS ec2 로드밸런서 health check 위한 api
@RestController
public class HealthChecker {
    @GetMapping("/health-check")
    public ApiResponse<?> healthCheck() {
        return ApiResponseCreator.success(HttpStatus.OK);
    }
}
