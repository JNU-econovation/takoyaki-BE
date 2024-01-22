package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationEditReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartiesPaginationResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyIdResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyInfoResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyListResDTO;
import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.config.properties.party.DurationUnit;
import com.bestbenefits.takoyaki.config.properties.party.PartyListType;
import com.bestbenefits.takoyaki.config.properties.user.UserType;
import com.bestbenefits.takoyaki.config.properties.user.YakiStatus;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.entity.Yaki;
import com.bestbenefits.takoyaki.exception.party.*;
import com.bestbenefits.takoyaki.repository.BookmarkRepository;
import com.bestbenefits.takoyaki.repository.PartyRepository;
import com.bestbenefits.takoyaki.repository.YakiRepositoy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final YakiRepositoy yakiRepository;
    private final UserService userService;
    private final BookmarkRepository bookmarkRepository;

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
        Party party = getPartyOrThrow(partyId);

        //party 상태 검사
        if (party.isDeleted()) {
            throw new PartyNotFoundException();
        }
        if (!party.isAuthor(id)) {
            throw new NotTakoException();
        }
        if (party.isClosed()) {
            throw new PartyClosedException();
        }

        Party newParty = partyCreationEditReqDTO.toEntity(user);

        //정책에 의한 파티 수정 조건검사
        if (!party.getCategory().equals(newParty.getCategory())) {
            throw new CategoryNotModifiableException(); //여기서만 사용됨
        }
        if (party.getRecruitNumber() > newParty.getRecruitNumber()) {
            throw new ModifiedRecruitNumberNotBiggerException(); //여기서만 사용됨
        }
        //TODO: 예상 마감일시와 예상 시작일시 비교 로직 정밀화 필요
        if (newParty.getPlannedClosingDate().isAfter(newParty.getPlannedClosingDate())) {
            throw new ModifiedPlannedClosingDateNotBeforeException(); //여기서만 사용됨
        }

        //영속성 컨텍스트 수정
        party.updateModifiedAt()
                .updateActivityLocation(newParty.getActivityLocation())
                .updateContactMethod(newParty.getContactMethod())
                .updateTitle(newParty.getTitle())
                .updateBody(newParty.getBody())
                .updateRecruitNumber(newParty.getRecruitNumber())
                .updatePlannedClosingDate(newParty.getPlannedClosingDate())
                .updatePlannedStartDate(newParty.getPlannedStartDate())
                .updateActivityDuration(newParty.getActivityDuration())
                .updateContact(newParty.getContact())
                .modify();

        return PartyIdResDTO.builder()
                .partyId(party.getId())
                .build();
    }

    @Transactional
    public PartyIdResDTO deleteParty(Long id, Long partyId) {
        Party party = getPartyOrThrow(partyId);

        if (party.isDeleted())
            throw new PartyNotFoundException();

        if (!party.isAuthor(id))
            throw new NotTakoException();

        if (party.isClosed())
            throw new PartyClosedException();

        party.updateModifiedAt().updateDeleteAt();
        bookmarkRepository.deleteAllByParty(party); //북마크 제거

        return PartyIdResDTO.builder()
                .partyId(party.getId())
                .build();
    }

    @Transactional
    public PartyIdResDTO closeParty(Long id, Long partyId) {
        Party p = getPartyOrThrow(partyId);

        if (p.isDeleted())
            throw new PartyNotFoundException();

        if (!p.isAuthor(id))
            throw new NotTakoException();

        if (p.isClosed())
            throw new PartyClosedException();


        p.updateModifiedAt().updateClosedAt();
        bookmarkRepository.deleteAllByParty(p); //북마크 제거

        return PartyIdResDTO.builder()
                .partyId(p.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public PartiesPaginationResDTO getPartiesInfoForPagination(boolean isLogin, Long id, int number, int pageNumber, Category category, ActivityLocation activityLocation) {
        User user = isLogin ? userService.getUserOrThrow(id) : null;
        Page<Object[]> page = partyRepository.getPartiesByFilteringAndPagination(PageRequest.of(pageNumber, number), user, category, activityLocation);

        List<Object[]> partyList = page.getContent();
        List<PartyListResDTO> partyDTOList = new ArrayList<>();

        for (Object[] row : partyList) {
            PartyListResDTO.PartyListResDTOBuilder builder = initializePartyListBuilder(row);
            if (isLogin) builder.bookmarked((boolean) row[10]); //row[10] == bookmarked
            partyDTOList.add(builder.build());
        }

        return new PartiesPaginationResDTO(partyDTOList, page.getTotalPages());
    }

    //TODO: row[] 인덱스 하드코딩 개선
    @Transactional(readOnly = true)
    public PartiesPaginationResDTO getPartiesInfoForLoginUser(Long id, String partyListType, int number, int pageNumber) {
        User user = userService.getUserOrThrow(id);

        Page<Object[]> page;

        PartyListType type = PartyListType.fromName(partyListType.replace("-", "_"));

        switch (type) {
            case NOT_CLOSED_WAITING ->
                    page = partyRepository.getNotClosedParties(PageRequest.of(pageNumber, number), user, YakiStatus.WAITING); //row[10] == bookmarked
            case NOT_CLOSED_ACCEPTED ->
                    page = partyRepository.getNotClosedParties(PageRequest.of(pageNumber, number), user, YakiStatus.ACCEPTED); //row[10] == bookmarked
            case CLOSED ->
                    page = partyRepository.getClosedParties(PageRequest.of(pageNumber, number), user); //row[10] 없음
            case WROTE ->
                    page = partyRepository.getWroteParties(PageRequest.of(pageNumber, number), user); //row[10] 없음
            case BOOKMARKED ->
                    page = partyRepository.getBookmarkedParties(PageRequest.of(pageNumber, number), user); //row[10] 없음
            default ->
                    page = null;
        }

        List<Object[]> partyList = page.getContent();

        List<PartyListResDTO> partyDTOList = new ArrayList<>();

        for (Object[] row : partyList) {
            PartyListResDTO.PartyListResDTOBuilder builder = initializePartyListBuilder(row);
            if (type == PartyListType.NOT_CLOSED_ACCEPTED || type == PartyListType.NOT_CLOSED_WAITING)
                builder.bookmarked((boolean) row[10]);
            partyDTOList.add(builder.build());
        }

        return new PartiesPaginationResDTO(partyDTOList, page.getTotalPages());
    }

    @Transactional(readOnly = true)
    public PartyInfoResDTO getPartyInfo(boolean isLogin, Long id, Long partyId) {
        User user = isLogin ? userService.getUserOrThrow(id) : null;
        Party party = partyRepository.findById(partyId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(PartyNotFoundException::new);

        //조회수 업데이트
        party.updateViewCount();

        //공통 제공 항목
        PartyInfoResDTO.PartyInfoResDTOBuilder builder = PartyInfoResDTO.builder()
                .partyId(partyId)
                .title(party.getTitle())
                .nickname(party.getUser().getNickname())
                .body(party.getBody())
                .category(party.getCategory().getName())
                .activityLocation(party.getActivityLocation().getName())
                .plannedStartDate(party.getPlannedStartDate())
                .activityDuration(DurationUnit.calculateDuration(party.getActivityDuration()))
                .contactMethod(party.getContactMethod().getName())
                .viewCount(party.getViewCount().intValue())
                .closedDate(party.getClosedAt().isEqual(party.getCreatedAt()) ? null : party.getClosedAt().toLocalDate()) //TODO:
                .recruitNumber(party.getRecruitNumber())
                .plannedClosingDate(party.getPlannedClosingDate());

        //요청한 유저가...
        //비로그인이면서 마감 안된 경우
        if (!isLogin) {
            if (!party.isClosed()) {
                return builder.userType(UserType.OTHER)
                        .build();
            }
            else throw new PartyNotFoundException();
        }

        //타코인 경우
        if (party.getUser() == user) {
            return builder.userType(UserType.TAKO)
                    .waitingList(yakiRepository.findWaitingList(party))
                    .acceptedList(yakiRepository.findAcceptedList(party))
                    .contact(party.getContact())
                    .build();
        }

        //야끼인 경우
        Yaki yaki = yakiRepository.findYakiByPartyAndUser(party, user).orElse(null);
        if (yaki != null) {
            if (party.isClosed()) {
                switch(yaki.getStatus()) {
                    case ACCEPTED -> builder.contact(party.getContact());
                    case WAITING -> throw new PartyNotFoundException();
                }
            }

            return builder.userType(UserType.YAKI)
                    .yakiStatus(yaki.getStatus())
                    .build();
        }

        //타코도 야끼도 아닌 경우
        if (party.isClosed())
            throw new PartyNotFoundException();
        else
            return builder.userType(UserType.OTHER) .build();
    }

    @Transactional(readOnly = true)
    public Party getPartyOrThrow(Long partyId) {
        return partyRepository.findById(partyId).orElseThrow(PartyNotFoundException::new);
    }

    private PartyListResDTO.PartyListResDTOBuilder initializePartyListBuilder(Object[] row) {
        int recruitNumber = (int) row[4]; //모집인원
        int waitingNumber = ((Long) row[7]).intValue(); //신청했고 대기중인 야끼
        int acceptedNumber = ((Long) row[8]).intValue(); //신청했고 수락된 야끼
        return PartyListResDTO.builder()
                .partyId((Long) row[0])
                .title((String) row[1])
                .category(((Category) row[2]).getName())
                .activityLocation(((ActivityLocation) row[3]).getName())
                .recruitNumber(recruitNumber)
                .plannedClosingDate(((LocalDate)row[5]).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .viewCount((Long) row[6])
                .waitingNumber(waitingNumber)
                .acceptedNumber(acceptedNumber)
                .occupationRate(String.format("%d/%d", acceptedNumber, recruitNumber))
                .closedDate(row[9] == null ? null : ((LocalDateTime)row[9]).toLocalDate())
                .competitionRate(String.format("%.1f", getCompetitionRate(recruitNumber, waitingNumber, acceptedNumber)));
    }

    private static float getCompetitionRate(int recruit, int waiting, int accepted) {
        if (recruit == accepted) return 0f;
        return (float) waiting / (recruit - accepted);
    }
}