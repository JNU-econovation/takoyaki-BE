package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.DTO.client.request.PartyCreationReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.PartyCreationResDTO;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.config.properties.party.ContactMethod;
import com.bestbenefits.takoyaki.config.properties.party.DurationUnit;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.repository.PartyRepository;
import com.bestbenefits.takoyaki.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.bestbenefits.takoyaki.config.properties.party.DurationUnit.*;

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
}