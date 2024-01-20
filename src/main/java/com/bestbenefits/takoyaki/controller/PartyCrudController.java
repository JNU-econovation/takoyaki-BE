package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationEditReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.PartyListReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartiesPaginationResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyInfoResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyListResDTO;
import com.bestbenefits.takoyaki.config.annotation.DontCareAuthentication;
import com.bestbenefits.takoyaki.config.annotation.NeedAuthentication;
import com.bestbenefits.takoyaki.config.annotation.Session;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.config.properties.party.PartyConst;
import com.bestbenefits.takoyaki.service.PartyService;
import com.bestbenefits.takoyaki.util.LoginChecker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PartyCrudController {
    private final PartyService partyService;
    private final LoginChecker loginChecker;

    /************ /party ************/
    @NeedAuthentication
    @PostMapping("/party")
    public ApiResponse<?> createParty(@Session(attribute = SessionConst.ID) Long id,
                                      @RequestBody @Valid PartyCreationEditReqDTO dto) {
        return ApiResponseCreator.success(partyService.createParty(id, dto), HttpStatus.CREATED);
    }

    /************ /parties ************/
    @DontCareAuthentication
    @GetMapping("/parties")
    public ApiResponse<?> getPartyCardListForMainPage(
            @Session(attribute = SessionConst.ID, nullable = true) Long id,
            @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
            @ModelAttribute @Valid PartyListReqDTO dto) {

        boolean isLogin = loginChecker.isLogin(id, authentication);
        Map<String, Object> meta = new HashMap<>();
        Map<String, List<PartyListResDTO>> data = new HashMap<>();

        //TODO: 커스텀 예외로 작성하기
        if (dto.getNumber() > PartyConst.MAX_PARTY_NUMBER_OF_REQUEST)
            throw new IllegalArgumentException("한 번에 요청할 수 있는 팟의 개수는 " + PartyConst.MAX_PARTY_NUMBER_OF_REQUEST + "개입니다.");

        Category category = Optional.ofNullable(dto.getCategoryName())
                    .map(Category::fromName)
                    .orElse(null);

        ActivityLocation activityLocation = Optional.ofNullable(dto.getActivityLocationName())
                    .map(ActivityLocation::fromName)
                    .orElse(null);

        PartiesPaginationResDTO ret = partyService.getPartiesInfoForPagination(
                isLogin,
                id,
                dto.getNumber(),
                dto.getPageNumber() - 1,
                category,
                activityLocation);


        data.put("card_list", ret.getPartyDTOlist());

        meta.put("is_login", isLogin);
        meta.put("count", data.get("card_list").size());
        meta.put("total_pages", ret.getTotalPages());

        return ApiResponseCreator.success(meta, data);
    }

    @NeedAuthentication
    @GetMapping("/parties/{party-type:\\D+}") //non-numeric url
    public ApiResponse<?> getSpecificParty(
            @Session(attribute = SessionConst.ID) Long id,
            @PathVariable(name = "party-type") String partyListType,
            @RequestParam int number,
            @RequestParam(name = "page-number") int pageNumber) {

        Map<String, Object> meta = new HashMap<>();
        Map<String, List<PartyListResDTO>> data = new HashMap<>();

        data.put("card_list", partyService.getPartiesInfoForLoginUser(id, partyListType, number, pageNumber - 1));
        meta.put("count", data.get("card_list").size());

        return ApiResponseCreator.success(meta, data);
    }

    @DontCareAuthentication
    @GetMapping("/parties/{party-id:\\d+}") //numeric url
    public ApiResponse<?> getPartyInfo(
            @Session(attribute = SessionConst.ID, nullable = true) Long id,
            @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
            @PathVariable(name = "party-id") Long partyId) {

        boolean isLogin = loginChecker.isLogin(id, authentication);
        PartyInfoResDTO dto = partyService.getPartyInfo(isLogin, id, partyId);

        Map<String, Object> meta = new HashMap<>();
        meta.put("is_login", isLogin);
        meta.put("user_type", dto.moveUserType());

        return ApiResponseCreator.success(meta, dto);
    }

    @NeedAuthentication
    @PatchMapping("parties/{party-id}")
    public ApiResponse<?> editParty(@Session(attribute = SessionConst.ID) Long id,
                                    @PathVariable(name = "party-id") Long partyId,
                                    @RequestBody @Valid PartyCreationEditReqDTO dto) {
        return ApiResponseCreator.success(partyService.editParty(id, partyId, dto));
    }

    @NeedAuthentication
    @DeleteMapping("/parties/{party-id}")
    public ApiResponse<?> deleteParty(@Session(attribute = SessionConst.ID) Long id,
                                      @PathVariable(name = "party-id") Long partyId) {
        return ApiResponseCreator.success(partyService.deleteParty(id, partyId));
    }

    @NeedAuthentication
    @PostMapping("/parties/{party-id}/closing")
    public ApiResponse<?> closeParty(@Session(attribute = SessionConst.ID) Long id,
                                     @PathVariable(name = "party-id") Long partyId) {
        return ApiResponseCreator.success(partyService.closeParty(id, partyId));
    }


}
