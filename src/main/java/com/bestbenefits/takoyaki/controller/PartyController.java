package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.CommentReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationEditReqDTO;
import com.bestbenefits.takoyaki.DTO.client.request.PartyListReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyInfoResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyListResDTO;
import com.bestbenefits.takoyaki.config.annotation.Session;
import com.bestbenefits.takoyaki.config.apiresponse.ResponseEntityCreator;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.config.properties.party.*;
import com.bestbenefits.takoyaki.exception.user.UnauthorizedException;
import com.bestbenefits.takoyaki.service.*;
import com.bestbenefits.takoyaki.util.LoginChecker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createParty(@Session(attribute = SessionConst.ID) Long id,
                                         @RequestBody @Valid PartyCreationEditReqDTO dto) {
        return ResponseEntityCreator.success(partyService.createParty(id, dto), HttpStatus.CREATED);
    }

    @GetMapping("/party/activity-location")
    public ResponseEntity<?> getActivityLocation() {
        Map<String, Object> data = new HashMap<>();
        data.put("activity_location", ActivityLocation.toNameList());
        return ResponseEntityCreator.success(data);
    }

    @GetMapping("/party/category")
    public ResponseEntity<?> getCategory() {
        Map<String, Object> data = new HashMap<>();
        data.put("category", Category.toNameList());
        return ResponseEntityCreator.success(data);
    }

    @GetMapping("/party/contact-method")
    public ResponseEntity<?> getContactMethod() {
        Map<String, Object> data = new HashMap<>();
        data.put("contact_method", ContactMethod.toNameList());
        return ResponseEntityCreator.success(data);
    }

    @GetMapping("/party/activity-duration-unit")
    public ResponseEntity<?> getActivityDurationUnit() {
        Map<String, Object> data = new HashMap<>();
        data.put("activity_duration_unit", DurationUnit.toNameList());
        return ResponseEntityCreator.success(data);
    }



    /************ /parties ************/
    @GetMapping("/parties/") //로그인 필요 X
    public ResponseEntity<?> getPartyCardListForMainPage(
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
                    throw new UnauthorizedException();
                partyDTOList = partyService.getPartiesInfoForLoginUser(id, dto.getPartyListType());
            }
        }

        return ResponseEntityCreator.success(partyDTOList);
    }

    @GetMapping("/parties/{party-id}") //로그인 필요 X
    public ResponseEntity<?> getPartyInfo(@Session(attribute = SessionConst.ID, nullable = true) Long id,
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

        return ResponseEntityCreator.success(partyInfoResDTO);
    }

    @PatchMapping("parties/{party-id}")
    public ResponseEntity<?> editParty(@Session(attribute = SessionConst.ID) Long id,
                                                @PathVariable(name = "party-id") Long partyId,
                                                @RequestBody @Valid PartyCreationEditReqDTO dto) {
        return ResponseEntityCreator.success(partyService.editParty(id, partyId, dto));
    }

    @DeleteMapping("/parties/{party-id}")
    public ResponseEntity<?> deleteParty(@Session(attribute = SessionConst.ID) Long id,
                                                  @PathVariable(name = "party-id") Long partyId) {
        return ResponseEntityCreator.success(partyService.deleteParty(id, partyId));
    }

    @PostMapping("/parties/{party-id}/closing")
    public ResponseEntity<?> closeParty(@Session(attribute = SessionConst.ID) Long id,
                                          @PathVariable(name = "party-id") Long partyId) {
        return ResponseEntityCreator.success(partyService.closeParty(id, partyId));
    }

    @PostMapping("/parties/{party-id}/apply")
    public ResponseEntity<?> applyToParty(@Session(attribute = SessionConst.ID) Long id,
                                                   @PathVariable(name = "party-id") Long partyId){
        yakiService.applyToParty(id, partyId);

        return ResponseEntityCreator.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/parties/{party-id}/apply")
    public ResponseEntity<?> cancelApplication(@Session(attribute = SessionConst.ID) Long id,
                                                        @PathVariable(name = "party-id") Long partyId){
        yakiService.cancelApplication(id, partyId);

        return ResponseEntityCreator.success();
    }

    @PostMapping("/parties/{party-id}/applicant/{user-id}")
    public ResponseEntity<?> acceptYaki(@Session(attribute = SessionConst.ID) Long id,
                                                 @PathVariable(name = "party-id") Long partyId,
                                                 @PathVariable(name = "user-id") Long yakiId){
        yakiService.acceptYaki(id, partyId, yakiId);

        return ResponseEntityCreator.success();
    }

    @DeleteMapping("/parties/{party-id}/applicant/{user-id}")
    public ResponseEntity<?> denyYaki(@Session(attribute = SessionConst.ID) Long id,
                                               @PathVariable(name = "party-id") Long partyId,
                                               @PathVariable(name = "user-id") Long yakiId){

        yakiService.denyYaki(id, partyId, yakiId);

        return ResponseEntityCreator.success();
    }

    @DeleteMapping("/parties/{party-id}/leaving")
    public ResponseEntity<?> leaveParty(@Session(attribute = SessionConst.ID) Long id,
                                                 @PathVariable(name = "party-id") Long partyId){

        yakiService.leaveParty(id, partyId);

        return ResponseEntityCreator.success();
    }

    @GetMapping("/parties/{party-id}/comment") //로그인 필요 X
    public ResponseEntity<?> getComment(@PathVariable(name = "party-id") Long partyId) {
        Map<String, Object> response = new HashMap<>();
        List<?> commentList = commentService.getComments(partyId);

        response.put("count", commentList.size());
        response.put("comment_list", commentList);
        return ResponseEntityCreator.success(response);
    }

    @PostMapping("/parties/{party-id}/comment")
    public ResponseEntity<?> addComment(@Session(attribute = SessionConst.ID) Long id,
                                                 @PathVariable(name = "party-id") Long partyId,
                                                 @RequestBody @Valid CommentReqDTO dto) {
        return ResponseEntityCreator.success(commentService.createComment(id, partyId, dto), HttpStatus.CREATED);
    }

    @PostMapping("/parties/{party-id}/bookmark")
    public ResponseEntity<?> addBookmark(@Session(attribute = SessionConst.ID) Long id,
                                                  @PathVariable(name = "party-id") Long partyId) {
        bookmarkService.addBookmark(id, partyId);
        return ResponseEntityCreator.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/parties/{party-id}/bookmark")
    public ResponseEntity<?> deleteBookmark(@Session(attribute = SessionConst.ID) Long id,
                                                     @PathVariable(name = "party-id") Long partyId) {
        bookmarkService.deleteBookmark(id, partyId);
        return ResponseEntityCreator.success();
    }
}