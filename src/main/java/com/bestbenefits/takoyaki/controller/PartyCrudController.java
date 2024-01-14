package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationEditReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.PartyListReqDTO;
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
import com.bestbenefits.takoyaki.exception.user.UnauthorizedException;
import com.bestbenefits.takoyaki.service.PartyService;
import com.bestbenefits.takoyaki.util.LoginChecker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    //TODO: 로그인 여부 확인 필요 없게 수정 필요...? 쿼리 param에서 login빼고 응답에서 로그인 여부 반환하게 하기
    //TODO: 응답에 meta에 적절한 정보 담기
    //TODO: 전체적으로 리팩터링 필요
    //TODO: 로그아웃 상태인데 비인가 오류 나는거 수정 필요
    @DontCareAuthentication
    @GetMapping("/parties")
    public ApiResponse<?> getPartyCardListForMainPage(
            @Session(attribute = SessionConst.ID, nullable = true) Long id,
            @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
            @ModelAttribute @Valid PartyListReqDTO dto){

        List<? extends PartyListResDTO> partyDTOList;
        boolean isLogin = loginChecker.isLogin(id, authentication);

        System.out.println("dto.getNumber() = " + dto.getNumber());

        switch (dto.getPartyListType()) {
            case ALL -> {
                if (dto.getNumber() > PartyConst.MAX_PARTY_NUMBER_OF_REQUEST)
                    throw new IllegalArgumentException("한 번에 요청할 수 있는 팟의 개수는 " + PartyConst.MAX_PARTY_NUMBER_OF_REQUEST + "개입니다.");

                Category category = Optional.ofNullable(dto.getCategoryName()).map(Category::fromName).orElse(null);
                ActivityLocation activityLocation = Optional.ofNullable(dto.getActivityLocationName()).map(ActivityLocation::fromName).orElse(null);

                if (dto.getLoginField() == isLogin)
                    partyDTOList = partyService.getPartiesInfoForPagination(isLogin, id, dto.getNumber(), dto.getPageNumber() - 1, category, activityLocation);
                else
                    throw new IllegalArgumentException("로그인 상태와 요청이 일치하지 않습니다.");
            }
            default -> {
                if (!loginChecker.isLogin(id, authentication)) {

                    System.out.println(">>>>> UnauthorizedException in PartyController");
                    throw new UnauthorizedException();
                }
                partyDTOList = partyService.getPartiesInfoForLoginUser(id, dto.getPartyListType());
            }
        }

        return ApiResponseCreator.success(partyDTOList);
    }


    //TODO: 로그인 여부 확인 필요 없게 수정 필요...? 쿼리 param에서 login빼고 응답에서 로그인 여부 반환하게 하기
    @DontCareAuthentication
    @GetMapping("/parties/{party-id}")
    public ApiResponse<?> getPartyInfo(@Session(attribute = SessionConst.ID, nullable = true) Long id,
                                       @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
                                       @RequestParam(name = "login") boolean loginField,
                                       @PathVariable(name = "party-id") Long partyId) {

        boolean isLogin = loginChecker.isLogin(id, authentication);

        PartyInfoResDTO partyInfoResDTO;

        if (loginField == isLogin)
            partyInfoResDTO = partyService.getPartyInfo(isLogin, id, partyId);
        else
            throw new IllegalArgumentException("로그인 상태와 요청이 일치하지 않습니다.");

        return ApiResponseCreator.success(partyInfoResDTO);
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
