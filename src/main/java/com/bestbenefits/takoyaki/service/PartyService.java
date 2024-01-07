package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyCreationResDTO;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final YakiRepositoy yakiRepository;

    @Transactional //
    public PartyCreationResDTO createParty(Long id, PartyCreationReqDTO partyCreationReqDTO) {
        User user = userRepository.findUserById(id).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        Party party = partyRepository.save(partyCreationReqDTO.toEntity(user));

        return PartyCreationResDTO.builder()
                .partyId(party.getId())
                .build();
    }

    @Transactional
    public void deleteParty(Long id, Long partyId) {
        Party p = partyRepository.findById(partyId).orElseThrow(
                () -> new IllegalArgumentException("Party ID가 잘못되었습니다."));

        if (!p.getUser().getId().equals(id))
            throw new IllegalArgumentException(String.format("해당 Party가 유저 ID: %d에 의해 생성되지 않았습니다.", id));

        if (p.getDeletedAt() != null)
            throw new IllegalArgumentException("이미 삭제된 Party입니다.");

        p.updateDeleteAt(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<PartyListResDTO> getParties(boolean isLogin, Long id, int number, int pageNumber, Category category, ActivityLocation activityLocation){

        List<Object[]> partyList;

        User user = isLogin ?
                userRepository.findUserById(id).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다.")) : null;
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
    public PartyInfoResDTO getParty(boolean isLogin, Long id, Long partyId){
        Party party = partyRepository.findById(partyId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팟입니다."));

        User user = isLogin ? userRepository.findUserById(id).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다.")) : null;

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

}