package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.CommentReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationEditReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.PartyListReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.CommentListResDTO;
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
import com.bestbenefits.takoyaki.service.YakiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class PartyController {
    private final PartyService partyService;
    private final YakiService yakiService;

    /************ /party ************/
    @PostMapping("/party")
    public ApiResponse<?> createParty(@Session(attribute = SessionConst.ID) Long id,
                                      @RequestBody @Valid PartyCreationEditReqDTO dto) {
        return ApiResponseCreator.success(partyService.createParty(id, dto));
    }

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




    /************ /parties ************/
    @GetMapping("/parties/")
//
    public ApiResponse<List<? extends PartyListResDTO>> getPartyCardListForMainPage(@Session(attribute = SessionConst.ID, nullable = true) Long id,
                                                   @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
                                                   @ModelAttribute @Valid PartyListReqDTO dto){
        List<? extends PartyListResDTO> partyDTOList = new ArrayList<>();

        boolean isLogin = (id != null && authentication != null && authentication);

        switch (dto.getPartyListType()) {
            case ALL -> {
                if (dto.getNumber() >= PartyConst.MAX_PARTY_NUMBER_OF_REQUEST)
                    throw new IllegalArgumentException("한 번에 요청할 수 있는 팟의 개수는 " + PartyConst.MAX_PARTY_NUMBER_OF_REQUEST + "개입니다.");

                Category category = Optional.ofNullable(dto.getCategoryName()).map(Category::fromName).orElse(null);
                ActivityLocation activityLocation = Optional.ofNullable(dto.getActivityLocationName()).map(ActivityLocation::fromName).orElse(null);

                if (dto.getLoginField() == isLogin)
                    partyDTOList = partyService.getPartiesInfoForPagination(isLogin, id, dto.getNumber(), dto.getPageNumber(), category, activityLocation);
                else
                    throw new IllegalArgumentException("로그인 상태와 요청이 일치하지 않습니다.");
            }
            case WAITING -> {

            }
        }

        return ApiResponseCreator.success(partyDTOList);
    }

    //TODO: 로그인 필요 없이도 접근할 수 있게 만들어야 함
    @GetMapping("/parties/{party-id}")
    public ApiResponse<PartyInfoResDTO> getParty(@Session(attribute = SessionConst.ID, nullable = true) Long id,
                                   @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
                                   @RequestParam(name = "login") boolean loginField,
                                   @PathVariable(name = "party-id") Long partyId){

        boolean isLogin = (id != null && authentication != null && authentication);

        PartyInfoResDTO partyInfoResDTO;

        if (loginField == isLogin)
            partyInfoResDTO = partyService.getPartyInfo(isLogin, id, partyId);
        else
            throw new IllegalArgumentException("로그인 상태와 요청이 일치하지 않습니다.");

        return ApiResponseCreator.success(partyInfoResDTO);
    }

    @PatchMapping("parties/{partyId}")
    public ApiResponse<?> editParty(@Session(attribute = SessionConst.ID) Long id,
                                    @PathVariable Long partyId,
                                    @RequestBody @Valid PartyCreationEditReqDTO dto) {
        return ApiResponseCreator.success(partyService.editParty(id, partyId, dto));
    }

    @DeleteMapping("/parties/{partyId}")
    public ApiResponse<?> deleteParty(@Session(attribute = SessionConst.ID) Long id,
                                      @PathVariable Long partyId) {
        return ApiResponseCreator.success(partyService.deleteParty(id, partyId));
    }

    @PostMapping("/parties/{partyId}/closing")
    ApiResponse<?> closeParty(@Session(attribute = SessionConst.ID) Long id,
                              @PathVariable Long partyId) {
        return ApiResponseCreator.success(partyService.closeParty(id, partyId));
    }

    @PostMapping("/parties/{party-id}/apply")
    public ApiResponse<?> applyToParty(@Session(attribute = SessionConst.ID) Long id,
                                       @PathVariable(name = "party-id") Long partyId){
        yakiService.applyToParty(id, partyId);

        return ApiResponseCreator.success(new ApiMessage("신청이 완료되었습니다."));
    }

    @DeleteMapping("/parties/{party-id}/apply")
    public ApiResponse<?> cancelApplication(@Session(attribute = SessionConst.ID) Long id,
                                            @PathVariable(name = "party-id") Long partyId){
        yakiService.cancelApplication(id, partyId);

        return ApiResponseCreator.success(new ApiMessage("신청이 취소되었습니다."));
    }

    @PostMapping("/parties/{party-id}/applicant/{user-id}")
    public ApiResponse<?> acceptYaki(@Session(attribute = SessionConst.ID) Long id,
                                     @PathVariable(name = "party-id") Long partyId,
                                     @PathVariable(name = "user-id") Long yakiId){
        yakiService.acceptYaki(id, partyId, yakiId);

        return ApiResponseCreator.success(new ApiMessage("야끼가 수락되었습니다."));
    }

    @DeleteMapping("/parties/{party-id}/applicant/{user-id}")
    public ApiResponse<?> denyYaki(@Session(attribute = SessionConst.ID) Long id,
                                   @PathVariable(name = "party-id") Long partyId,
                                   @PathVariable(name = "user-id") Long yakiId){

        yakiService.denyYaki(id, partyId, yakiId);

        return ApiResponseCreator.success(new ApiMessage("야끼가 거절되었습니다."));
    }

    @DeleteMapping("/parties/{party-id}/leaving")
    public ApiResponse<?> leaveParty(@Session(attribute = SessionConst.ID) Long id,
                                     @PathVariable(name = "party-id") Long partyId){

        yakiService.leaveParty(id, partyId);

        return ApiResponseCreator.success(new ApiMessage("팟에서 나가졌습니다."));
    }

    @GetMapping("/parties/{partyId}/comment")
    public ApiResponse<?> getComment(@PathVariable Long partyId) {
        //TODO: 댓글 리스트 반환 필요
        return ApiResponseCreator.success(new CommentListResDTO(null));
    }

    @PostMapping("/parties/{partyId}/comment")
    public ApiResponse<?> addComment(@PathVariable Long partyId,
                                     @RequestBody CommentReqDTO commentReqDTO) {
        //TODO: 댓글 작성 구현하기
        return ApiResponseCreator.success(null);
    }
}