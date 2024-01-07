package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.entity.Yaki;
import com.bestbenefits.takoyaki.repository.PartyRepository;
import com.bestbenefits.takoyaki.repository.UserRepository;
import com.bestbenefits.takoyaki.repository.YakiRepositoy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class YakiService {
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final YakiRepositoy yakiRepositoy;

    @Transactional
    public void applyToParty(Long id, Long partyId){
        User user = userRepository.findUserById(id).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        Party party = partyRepository.findById(partyId)
                .filter(p -> p.getDeletedAt() == null)
                .filter(p -> p.getCreatedAt().isEqual(p.getClosedAt()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 마감된 팟입니다."));
        if (yakiRepositoy.existsYakiByPartyAndUser(party, user))
            throw new IllegalArgumentException("이미 신청한 팟입니다.");
        yakiRepositoy.save(new Yaki(user, party));
    }

}
