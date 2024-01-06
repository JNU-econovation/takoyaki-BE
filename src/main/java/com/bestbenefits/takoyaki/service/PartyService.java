package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyCreationResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyListForLoginUserResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyListResDTO;
import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.repository.PartyRepository;
import com.bestbenefits.takoyaki.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public List<PartyListResDTO> getParties(int number, int pageNumber, Category category, ActivityLocation activityLocation){
        List<Object[]> partyList = partyRepository.getPartiesByFiltering(PageRequest.of(pageNumber, number), category, activityLocation).getContent();
        List<PartyListResDTO> partyDTOList = new ArrayList<>();

        for (Object[] row : partyList) {
            int recruitNumber = (int) row[4];
            int waitingNumber = ((Long) row[6]).intValue();
            int acceptedNumber = ((Long) row[7]).intValue();
            float competitionRate = (waitingNumber != 0) ? (float) (recruitNumber - acceptedNumber)/waitingNumber : 0f;
            partyDTOList.add(
                    PartyListResDTO.builder()
                            .partyId((Long) row[0])
                            .title((String) row[1])
                            .category(((Category) row[2]).getName())
                            .activityLocation(((ActivityLocation) row[3]).getName())
                            .recruitNumber(recruitNumber)
                            .plannedClosingDate((LocalDate) row[5])
                            .waitingNumber(waitingNumber)
                            .acceptedNumber(acceptedNumber)
                            .competitionRate(competitionRate)
                            .build()
            );
        }
        return partyDTOList;
    }

    @Transactional(readOnly = true)
    public List<PartyListForLoginUserResDTO> getParties(Long id, int number, int pageNumber, Category category, ActivityLocation activityLocation){
        User user = userRepository.findUserById(id).orElseThrow(
                ()->new IllegalArgumentException("유저가 없습니다."));

        List<Object[]> partyList = partyRepository.getPartiesWithBookmarkFlagByFiltering(user, PageRequest.of(pageNumber, number), category, activityLocation).getContent();
        List<PartyListForLoginUserResDTO> partyDTOList = new ArrayList<>();

        for (Object[] row : partyList) {
            int recruitNumber = (int) row[4];
            int waitingNumber = ((Long) row[6]).intValue();
            int acceptedNumber = ((Long) row[7]).intValue();
            float competitionRate = (waitingNumber != 0) ? (float) (recruitNumber - acceptedNumber)/waitingNumber : 0f;
            partyDTOList.add(
                    PartyListForLoginUserResDTO.builder()
                            .partyId((Long) row[0])
                            .title((String) row[1])
                            .category(((Category) row[2]).getName())
                            .activityLocation(((ActivityLocation) row[3]).getName())
                            .recruitNumber(recruitNumber)
                            .plannedClosingDate((LocalDate) row[5])
                            .waitingNumber(waitingNumber)
                            .acceptedNumber(acceptedNumber)
                            .competitionRate(competitionRate)
                            .bookmarked((boolean) row[8])
                            .build()
            );
        }
        return partyDTOList;
    }
}