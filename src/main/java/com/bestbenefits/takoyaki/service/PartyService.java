package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationEditReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyIdResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyInfoResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyListResDTO;
import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.config.properties.party.DurationUnit;
import com.bestbenefits.takoyaki.config.properties.user.UserType;
import com.bestbenefits.takoyaki.config.properties.user.YakiStatus;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.entity.Yaki;
import com.bestbenefits.takoyaki.repository.PartyRepository;
import com.bestbenefits.takoyaki.repository.UserRepository;
import com.bestbenefits.takoyaki.repository.YakiRepositoy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final YakiRepositoy yakiRepository;
    private final UserService userService;

    @Transactional
    public PartyIdResDTO createParty(Long id, PartyCreationEditReqDTO partyCreationEditReqDTO) {
        User user = userService.getUserOrThrow(id);
        Party party = partyRepository.save(partyCreationEditReqDTO.toEntity(user));

        return PartyIdResDTO.builder()
                .partyId(party.getId())
                .build();
    }

    @Transactional
    public PartyIdResDTO editParty(Long id, Long partyId, PartyCreationEditReqDTO partyCreationEditReqDTO) {
        //ID 유효성 검사
        User user = userService.getUserOrThrow(id);
        Party party = partyRepository.findById(partyId).orElseThrow(
                () -> new IllegalArgumentException("잘못된 파티 ID입니다."));

        //party 상태 검사
        if (!party.isAuthor(id)) {
            throw new IllegalArgumentException(String.format("해당 Party가 유저 ID: %d에 의해 생성되지 않았습니다.", id));
        }
        if (party.isDeleted()) {
            throw new IllegalArgumentException("이미 삭제된 파티는 수정할 수 없습니다.");
        }
        if (party.isClosed()) {
            throw new IllegalArgumentException("이미 마감된 파티는 수정할 수 없습니다.");
        }

        Party newParty = partyCreationEditReqDTO.toEntity(user);

        //정책에 의한 파티 수정 조건검사
        if (!party.getCategory().equals(newParty.getCategory())) {
            throw new IllegalArgumentException("카테고리는 수정할 수 없습니다.");
        }
        if (party.getRecruitNumber() > newParty.getRecruitNumber()) {
            throw new IllegalArgumentException("수정 후 모집인원은 기존보다 같거나 커야 합니다.");
        }
        //TODO: 예상 마감일시와 예상 시작일시 비교 로직 정밀화 필요
        if (newParty.getPlannedClosingDate().isAfter(newParty.getPlannedClosingDate())) {
            throw new IllegalArgumentException("예상 마감 일시는 예상 시작 일시보다 이전이어야 합니다.");
        }

        //영속성 컨텍스트 수정
        party.updateModifiedAt().
                updateActivityLocation(newParty.getActivityLocation())
                .updateContactMethod(newParty.getContactMethod())
                .updateTitle(newParty.getTitle())
                .updateBody(newParty.getBody())
                .updateRecruitNumber(newParty.getRecruitNumber())
                .updatePlannedClosingDate(newParty.getPlannedClosingDate())
                .updatePlannedStartDate(newParty.getPlannedStartDate())
                .updateActivityDuration(newParty.getActivityDuration())
                .updateContact(newParty.getContact());

        return PartyIdResDTO.builder()
                .partyId(party.getId())
                .build();
    }

    @Transactional
    public PartyIdResDTO deleteParty(Long id, Long partyId) {
        Party p = partyRepository.findById(partyId).orElseThrow(
                () -> new IllegalArgumentException("Party ID가 잘못되었습니다."));

        if (!p.isAuthor(id))
            throw new IllegalArgumentException(String.format("해당 Party가 유저 ID: %d에 의해 생성되지 않았습니다.", id));

        if (p.isDeleted())
            throw new IllegalArgumentException("이미 삭제된 Party입니다.");

        if (p.isClosed())
            throw new IllegalArgumentException("이미 마감된 Party는 삭제할 수 없습니다.");

        p.updateModifiedAt().updateDeleteAt();

        return PartyIdResDTO.builder()
                .partyId(p.getId())
                .build();
    }

    @Transactional
    public PartyIdResDTO closeParty(Long id, Long partyId) {
        Party p = partyRepository.findById(partyId).orElseThrow(
                () -> new IllegalArgumentException("Party ID가 잘못되었습니다."));

        if (!p.isAuthor(id))
            throw new IllegalArgumentException(String.format("해당 Party가 유저 ID: %d에 의해 생성되지 않았습니다.", id));

        if (p.isDeleted())
            throw new IllegalArgumentException("이미 삭제된 Party는 마감할 수 없습니다.");

        if (p.isClosed())
            throw new IllegalArgumentException("이미 마감된 Party입니다.");

        p.updateModifiedAt().updateClosedAt();

        return PartyIdResDTO.builder()
                .partyId(p.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PartyListResDTO> getParties(boolean isLogin, Long id, int number, int pageNumber, Category category, ActivityLocation activityLocation){

        List<Object[]> partyList;

        User user = isLogin ? userService.getUserOrThrow(id) : null;
        partyList = partyRepository.getPartiesByFiltering(PageRequest.of(pageNumber, number), user, category, activityLocation).getContent();

        List<PartyListResDTO> partyDTOList = new ArrayList<>();

        for (Object[] row : partyList) {
            int recruitNumber = (int) row[4];
            int waitingNumber = ((Long) row[6]).intValue();
            int acceptedNumber = ((Long) row[7]).intValue();
            float competitionRate = (waitingNumber != 0) ? (float) (recruitNumber - acceptedNumber)/waitingNumber : 0f;
            PartyListResDTO.PartyListResDTOBuilder builder = PartyListResDTO.builder()
                    .partyId((Long) row[0])
                    .title((String) row[1])
                    .category(((Category) row[2]).getName())
                    .activityLocation(((ActivityLocation) row[3]).getName())
                    .recruitNumber(recruitNumber)
                    .plannedClosingDate((LocalDate) row[5])
                    .waitingNumber(waitingNumber)
                    .acceptedNumber(acceptedNumber)
                    .competitionRate(competitionRate);
            if (isLogin) builder.bookmarked((boolean) row[8]);
            partyDTOList.add(builder.build());
        }

        return partyDTOList;
    }

    @Transactional(readOnly = true)
    public PartyInfoResDTO getPartyInfo(boolean isLogin, Long id, Long partyId){
        Party party = partyRepository.findById(partyId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팟입니다."));

        User user = isLogin ? userService.getUserOrThrow(id) : null;

        PartyInfoResDTO.PartyInfoResDTOBuilder builder =
                PartyInfoResDTO.builder().partyId(partyId)
                        .title(party.getTitle())
                        .nickname(party.getUser().getNickname())
                        .body(party.getBody())
                        .category(party.getCategory().getName())
                        .activityLocation(party.getActivityLocation().getName())
                        .plannedStartDate(party.getPlannedStartDate())
                        .activityDuration(DurationUnit.calculateDuration(party.getActivityDuration()))
                        .contactMethod(party.getContactMethod().getName())
                        .viewCount(party.getViewCount().intValue())
                        .closedDate(party.getClosedAt().isEqual(party.getCreatedAt()) ? null : party.getClosedAt().toLocalDate() ) //
                        .recruitNumber(party.getRecruitNumber())
                        .plannedClosingDate(party.getPlannedClosingDate());

        if (isLogin){
            UserType userType;
            if (party.getUser() == user) {
                userType = UserType.TAKO;
                builder.waitingList(yakiRepository.findWaitingList(party))
                        .acceptedList(yakiRepository.findAcceptedList(party))
                        .contact(party.getContact());
            }else{
                Yaki yaki = yakiRepository.findYakiByPartyAndUser(party, user).orElse(null);
                userType = (yaki != null) ? UserType.YAKI : UserType.OTHER;
                builder.yakiStatus(yaki.getStatus());
                if (party.getClosedAt() != null && yaki.getStatus() == YakiStatus.ACCEPTED)
                    builder.contact(party.getContact());
            }
            builder.userType(userType);
        }

        return builder.build();
    }

    @Transactional(readOnly = true)
    public Party getPartyOrThrow(Long partyId){
        return partyRepository.findById(partyId).orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 마감된 팟입니다."));
    }

}