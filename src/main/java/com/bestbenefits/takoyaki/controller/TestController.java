package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationEditReqDTO;
import com.bestbenefits.takoyaki.config.annotation.Session;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.config.properties.party.ContactMethod;
import com.bestbenefits.takoyaki.config.properties.party.DurationUnit;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final PartyController partyController;
    private final UserService userService;

    @PostMapping("/users/login/{id}")
    public ApiResponse<?> tempLogin(HttpSession session, @PathVariable Long id){
        User user = userService.tempLogin(id);
        session.setAttribute(SessionConst.ID, id);
        session.setAttribute(SessionConst.AUTHENTICATION, true);
        return ApiResponseCreator.success(user);
    }

    @PostMapping("/users/signup")
    public ApiResponse<?> tempSignUp(HttpSession session){
        User user = userService.tempSignUp();
        session.setAttribute(SessionConst.ID, user.getId());
        session.setAttribute(SessionConst.AUTHENTICATION, true);
        return ApiResponseCreator.success(user);
    }

    @GetMapping("/party/get-random")
    public PartyCreationEditReqDTO getRandomParty() {
        Random r = new Random();
        int dayInterval = 50;
        LocalDate randomPlannedClose = LocalDate.now().plusDays(r.nextInt(dayInterval) + 1);
        ContactMethod randomContactMethod = ContactMethod.values()[r.nextInt(ContactMethod.values().length)];
        int randomContents = r.nextInt(999999);

        return PartyCreationEditReqDTO.builder()
                .category(Category.toNameList().get(
                        r.nextInt(Category.toNameList().size())))
                .activityLocation(ActivityLocation.toNameList().get(
                        r.nextInt(ActivityLocation.toNameList().size())))
                .contactMethod(randomContactMethod.getName())
                .activityDurationUnit(DurationUnit.toNameList().get(
                        r.nextInt(DurationUnit.toNameList().size())))
                .activityDuration(r.nextInt(31) + 1)
                .title("랜덤 제목 "+ randomContents)
                .body("랜덤 본문 "+ randomContents)
                .recruitNumber(r.nextInt(14) + 1)
                .plannedClosingDate(randomPlannedClose)
                .plannedStartDate(randomPlannedClose.plusDays(r.nextInt(6*30 - dayInterval)))
                .contact(getRandomContact(randomContactMethod))
                .build();
    }

    @PostMapping("/party/post-random")
    public ApiResponse<?> postRandomParty(@Session(attribute = SessionConst.ID) Long id) {
        return partyController.createParty(id, getRandomParty());
    }

    public static String getRandomContact(ContactMethod m) {
        Random r = new Random();
        switch(m) {
            case KAKAO_OPENCHATTING -> {
                return "https://openchat.kakao.com/" + Math.abs(r.nextInt());
            }
            case EMAIL -> {
                return "nick" + r.nextInt(9999) + "@example.com";
            }
            case CELLPHONE_NUMBER -> {
                return String.format("010-%d-%d", r.nextInt(1000, 10000), r.nextInt(1000, 10000));
            }
            default -> {
                return "";
            }
        }
    }


}
