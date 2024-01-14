package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.config.annotation.NeedAuthentication;
import com.bestbenefits.takoyaki.config.annotation.Session;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.service.YakiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/parties/{party-id}")
public class YakiController {
    private final YakiService yakiService;

    @NeedAuthentication
    @PostMapping("/apply")
    public ApiResponse<?> applyToParty(@Session(attribute = SessionConst.ID) Long id,
                                       @PathVariable(name = "party-id") Long partyId){
        yakiService.applyToParty(id, partyId);

        return ApiResponseCreator.success(HttpStatus.CREATED);
    }

    @NeedAuthentication
    @DeleteMapping("/apply")
    public ApiResponse<?> cancelApplication(@Session(attribute = SessionConst.ID) Long id,
                                            @PathVariable(name = "party-id") Long partyId) {
        yakiService.cancelApplication(id, partyId);

        return ApiResponseCreator.success();
    }

    @NeedAuthentication
    @DeleteMapping("/leaving")
    public ApiResponse<?> leaveParty(@Session(attribute = SessionConst.ID) Long id,
                                     @PathVariable(name = "party-id") Long partyId) {

        yakiService.leaveParty(id, partyId);

        return ApiResponseCreator.success();
    }

    @NeedAuthentication
    @PostMapping("/applicant/{user-id}")
    public ApiResponse<?> acceptYaki(@Session(attribute = SessionConst.ID) Long id,
                                     @PathVariable(name = "party-id") Long partyId,
                                     @PathVariable(name = "user-id") Long yakiId) {
        yakiService.acceptYaki(id, partyId, yakiId);

        return ApiResponseCreator.success();
    }

    @NeedAuthentication
    @DeleteMapping("/applicant/{user-id}")
    public ApiResponse<?> denyYaki(@Session(attribute = SessionConst.ID) Long id,
                                   @PathVariable(name = "party-id") Long partyId,
                                   @PathVariable(name = "user-id") Long yakiId) {

        yakiService.denyYaki(id, partyId, yakiId);

        return ApiResponseCreator.success();
    }

}
