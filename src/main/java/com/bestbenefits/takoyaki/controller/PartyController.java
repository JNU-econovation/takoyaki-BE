package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyInfoResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyListResDTO;
import com.bestbenefits.takoyaki.config.annotation.Session;
import com.bestbenefits.takoyaki.config.apiresponse.ApiMessage;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.config.properties.party.*;
import com.bestbenefits.takoyaki.service.PartyService;
import com.bestbenefits.takoyaki.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class PartyController {
    private final PartyService partyService;
    private final UserService userService;

    @GetMapping("/party/activity-location")
    public ApiResponse<?> getActivityLocation() {
        Map<String, Object> data = new HashMap<>();
        data.put("activity_location", ActivityLocation.toNameList());
        return ApiResponseCreator.success(data);
    }

    @GetMapping("/party/category")
    public ApiResponse<?> getCategory() {
        Map<String, Object> data = new HashMap<>();
        data.put("category", Category.toNameList());
        return ApiResponseCreator.success(data);
    }

    @GetMapping("/party/contact-method")
    public ApiResponse<?> getContactMethod() {
        Map<String, Object> data = new HashMap<>();
        data.put("contact_method", ContactMethod.toNameList());
        return ApiResponseCreator.success(data);
    }

    @GetMapping("/party/activity-duration-unit")
    public ApiResponse<?> getActivityDurationUnit() {
        Map<String, Object> data = new HashMap<>();
        data.put("activity_duration_unit", DurationUnit.toNameList());
        return ApiResponseCreator.success(data);
    }

    @PostMapping("/party")
    public ApiResponse<?> createParty(@Session(attribute = SessionConst.ID) Long id, @RequestBody @Valid PartyCreationReqDTO dto) {
        return ApiResponseCreator.success(partyService.createParty(id, dto));
    }

    @DeleteMapping("/parties/{partyId}")
    public ApiResponse<?> deleteParty(@Session(attribute = SessionConst.ID) Long id, @PathVariable Long partyId) {
        partyService.deleteParty(id, partyId);
        return ApiResponseCreator.success(new ApiMessage(String.format("파티 id: %d번이 삭제되었습니다.", partyId)));
    }

    @GetMapping("/parties/all")
    public ApiResponse<List<? extends PartyListResDTO>> getParties(@Session(attribute = SessionConst.ID, nullable = true) Long id,
                                                   @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
                                                   @RequestParam(name = "login") boolean loginField,
                                                   @RequestParam int number,
                                                   @RequestParam(name = "page_number") int pageNumber,
                                                   @RequestParam(required = false) String categoryName,
                                                   @RequestParam(name = "activity_location", required = false) String activityLocationName){

        if (number >= PartyConst.MAX_PARTY_NUMBER_OF_REQUEST)
            throw new IllegalArgumentException("한 번에 요청할 수 있는 팟의 개수는 "+PartyConst.MAX_PARTY_NUMBER_OF_REQUEST+"개입니다.");

        Category category = Optional.ofNullable(categoryName).map(Category::fromName).orElse(null);
        ActivityLocation activityLocation = Optional.ofNullable(activityLocationName).map(ActivityLocation::fromName).orElse(null);

        List<? extends PartyListResDTO> partyDTOList;

        boolean isLogin = (id != null && authentication != null && authentication);

        if (loginField == isLogin)
            partyDTOList = partyService.getParties(isLogin, id, number, pageNumber, category, activityLocation);
        else
            throw new IllegalArgumentException("로그인 상태와 요청이 일치하지 않습니다.");

        return ApiResponseCreator.success(partyDTOList);
    }

    @GetMapping("/parties/{party-id}")
    public ApiResponse<?> getParty(@Session(attribute = SessionConst.ID, nullable = true) Long id,
                                   @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
                                   @RequestParam(name = "login") boolean loginField,
                                   @PathVariable(name = "party-id") Long partyId
                                   ){
        //삭제된 게시글인 경우 404 예외 발생

        boolean isLogin = (id != null && authentication != null && authentication);

        PartyInfoResDTO partyInfoResDTO;

        if (loginField == isLogin)
            partyInfoResDTO = partyService.getParty(isLogin, id, partyId);
        else
            throw new IllegalArgumentException("로그인 상태와 요청이 일치하지 않습니다.");

        return ApiResponseCreator.success(partyInfoResDTO);
    }

}