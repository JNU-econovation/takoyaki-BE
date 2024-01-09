package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.CommentReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationEditReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.PartyListReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyInfoResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyListResDTO;
import com.bestbenefits.takoyaki.config.annotation.Session;
import com.bestbenefits.takoyaki.config.apiresponse.DEPRECATED__ApiMessage;
import com.bestbenefits.takoyaki.config.apiresponse.DEPRECATED__ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.DEPRECATED__ApiResponseCreator;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.config.properties.party.*;
import com.bestbenefits.takoyaki.exception.NeedLoginException;
import com.bestbenefits.takoyaki.interceptor.AuthenticationCheckInterceptor;
import com.bestbenefits.takoyaki.service.*;
import com.bestbenefits.takoyaki.util.LoginChecker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class PartyController {
    private final PartyService partyService;
    private final YakiService yakiService;
    private final CommentService commentService;
    private final BookmarkService bookmarkService;

    /************ /party ************/
    @PostMapping("/party")
    public DEPRECATED__ApiResponse<?> createParty(@Session(attribute = SessionConst.ID) Long id,
                                                  @RequestBody @Valid PartyCreationEditReqDTO dto) {
        return DEPRECATED__ApiResponseCreator.success(partyService.createParty(id, dto));
    }

    @GetMapping("/party/activity-location")
    public DEPRECATED__ApiResponse<?> getActivityLocation() {
        Map<String, Object> data = new HashMap<>();
        data.put("activity_location", ActivityLocation.toNameList());
        return DEPRECATED__ApiResponseCreator.success(data);
    }

    @GetMapping("/party/category")
    public DEPRECATED__ApiResponse<?> getCategory() {
        Map<String, Object> data = new HashMap<>();
        data.put("category", Category.toNameList());
        return DEPRECATED__ApiResponseCreator.success(data);
    }

    @GetMapping("/party/contact-method")
    public DEPRECATED__ApiResponse<?> getContactMethod() {
        Map<String, Object> data = new HashMap<>();
        data.put("contact_method", ContactMethod.toNameList());
        return DEPRECATED__ApiResponseCreator.success(data);
    }

    @GetMapping("/party/activity-duration-unit")
    public DEPRECATED__ApiResponse<?> getActivityDurationUnit() {
        Map<String, Object> data = new HashMap<>();
        data.put("activity_duration_unit", DurationUnit.toNameList());
        return DEPRECATED__ApiResponseCreator.success(data);
    }



    /************ /parties ************/
    @GetMapping("/parties/") //로그인 필요 X
    public DEPRECATED__ApiResponse<List<? extends PartyListResDTO>> getPartyCardListForMainPage(
            @Session(attribute = SessionConst.ID, nullable = true) Long id,
            @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
            @ModelAttribute @Valid PartyListReqDTO dto){
        //TODO: 로그인 여부 확인 필요 없게 수정 필요...? 쿼리 param에서 login빼고 응답에서 로그인 여부 반환하게 하기

        List<? extends PartyListResDTO> partyDTOList;
        boolean isLogin = LoginChecker.isLogin(id, authentication);

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
            default -> {
                if (!LoginChecker.isLogin(id, authentication))
                    throw new NeedLoginException();
                partyDTOList = partyService.getPartiesInfoForLoginUser(id, dto.getPartyListType());
            }
        }

        return DEPRECATED__ApiResponseCreator.success(partyDTOList);
    }

    @GetMapping("/parties/{party-id}") //로그인 필요 X
    public DEPRECATED__ApiResponse<PartyInfoResDTO> getPartyInfo(@Session(attribute = SessionConst.ID, nullable = true) Long id,
                                                                 @Session(attribute = SessionConst.AUTHENTICATION, nullable = true) Boolean authentication,
                                                                 @RequestParam(name = "login") boolean loginField,
                                                                 @PathVariable(name = "party-id") Long partyId) {
        //TODO: 로그인 여부 확인 필요 없게 수정 필요...? 쿼리 param에서 login빼고 응답에서 로그인 여부 반환하게 하기

        boolean isLogin = LoginChecker.isLogin(id, authentication);

        PartyInfoResDTO partyInfoResDTO;

        if (loginField == isLogin)
            partyInfoResDTO = partyService.getPartyInfo(isLogin, id, partyId);
        else
            throw new IllegalArgumentException("로그인 상태와 요청이 일치하지 않습니다.");

        return DEPRECATED__ApiResponseCreator.success(partyInfoResDTO);
    }

    @PatchMapping("parties/{party-id}")
    public DEPRECATED__ApiResponse<?> editParty(@Session(attribute = SessionConst.ID) Long id,
                                                @PathVariable(name = "party-id") Long partyId,
                                                @RequestBody @Valid PartyCreationEditReqDTO dto) {
        return DEPRECATED__ApiResponseCreator.success(partyService.editParty(id, partyId, dto));
    }

    @DeleteMapping("/parties/{party-id}")
    public DEPRECATED__ApiResponse<?> deleteParty(@Session(attribute = SessionConst.ID) Long id,
                                                  @PathVariable(name = "party-id") Long partyId) {
        return DEPRECATED__ApiResponseCreator.success(partyService.deleteParty(id, partyId));
    }

    @PostMapping("/parties/{party-id}/closing")
    DEPRECATED__ApiResponse<?> closeParty(@Session(attribute = SessionConst.ID) Long id,
                                          @PathVariable(name = "party-id") Long partyId) {
        return DEPRECATED__ApiResponseCreator.success(partyService.closeParty(id, partyId));
    }

    @PostMapping("/parties/{party-id}/apply")
    public DEPRECATED__ApiResponse<?> applyToParty(@Session(attribute = SessionConst.ID) Long id,
                                                   @PathVariable(name = "party-id") Long partyId){
        yakiService.applyToParty(id, partyId);

        return DEPRECATED__ApiResponseCreator.success(new DEPRECATED__ApiMessage("신청이 완료되었습니다."));
    }

    @DeleteMapping("/parties/{party-id}/apply")
    public DEPRECATED__ApiResponse<?> cancelApplication(@Session(attribute = SessionConst.ID) Long id,
                                                        @PathVariable(name = "party-id") Long partyId){
        yakiService.cancelApplication(id, partyId);

        return DEPRECATED__ApiResponseCreator.success(new DEPRECATED__ApiMessage("신청이 취소되었습니다."));
    }

    @PostMapping("/parties/{party-id}/applicant/{user-id}")
    public DEPRECATED__ApiResponse<?> acceptYaki(@Session(attribute = SessionConst.ID) Long id,
                                                 @PathVariable(name = "party-id") Long partyId,
                                                 @PathVariable(name = "user-id") Long yakiId){
        yakiService.acceptYaki(id, partyId, yakiId);

        return DEPRECATED__ApiResponseCreator.success(new DEPRECATED__ApiMessage("야끼가 수락되었습니다."));
    }

    @DeleteMapping("/parties/{party-id}/applicant/{user-id}")
    public DEPRECATED__ApiResponse<?> denyYaki(@Session(attribute = SessionConst.ID) Long id,
                                               @PathVariable(name = "party-id") Long partyId,
                                               @PathVariable(name = "user-id") Long yakiId){

        yakiService.denyYaki(id, partyId, yakiId);

        return DEPRECATED__ApiResponseCreator.success(new DEPRECATED__ApiMessage("야끼가 거절되었습니다."));
    }

    @DeleteMapping("/parties/{party-id}/leaving")
    public DEPRECATED__ApiResponse<?> leaveParty(@Session(attribute = SessionConst.ID) Long id,
                                                 @PathVariable(name = "party-id") Long partyId){

        yakiService.leaveParty(id, partyId);

        return DEPRECATED__ApiResponseCreator.success(new DEPRECATED__ApiMessage("팟에서 나가졌습니다."));
    }

    @GetMapping("/parties/{party-id}/comment") //로그인 필요 X
    public DEPRECATED__ApiResponse<?> getComment(@PathVariable(name = "party-id") Long partyId) {
        Map<String, Object> response = new HashMap<>();
        List<?> commentList = commentService.getComments(partyId);

        response.put("count", commentList.size());
        response.put("comment_list", commentList);
        return DEPRECATED__ApiResponseCreator.success(response);
    }

    @PostMapping("/parties/{party-id}/comment")
    public DEPRECATED__ApiResponse<?> addComment(@Session(attribute = SessionConst.ID) Long id,
                                                 @PathVariable(name = "party-id") Long partyId,
                                                 @RequestBody @Valid CommentReqDTO dto) {
        return DEPRECATED__ApiResponseCreator.success(commentService.createComment(id, partyId, dto));
    }

    @PostMapping("/parties/{party-id}/bookmark")
    public DEPRECATED__ApiResponse<?> addBookmark(@Session(attribute = SessionConst.ID) Long id,
                                                  @PathVariable(name = "party-id") Long partyId) {
        bookmarkService.addBookmark(id, partyId);
        return DEPRECATED__ApiResponseCreator.success(new DEPRECATED__ApiMessage("북마크되었습니다."));
    }

    @DeleteMapping("/parties/{party-id}/bookmark")
    public DEPRECATED__ApiResponse<?> deleteBookmark(@Session(attribute = SessionConst.ID) Long id,
                                                     @PathVariable(name = "party-id") Long partyId) {
        bookmarkService.deleteBookmark(id, partyId);
        return DEPRECATED__ApiResponseCreator.success(new DEPRECATED__ApiMessage("북마크 제거되었습니다."));
    }
}