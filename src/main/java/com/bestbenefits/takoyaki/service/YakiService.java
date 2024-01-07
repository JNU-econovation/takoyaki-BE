package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.config.properties.user.YakiStatus;
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
    private final UserService userService;
    private final PartyService partyService;
    private final YakiRepositoy yakiRepositoy;

    @Transactional
    public void applyToParty(Long id, Long partyId){
        User user = userService.getUserOrThrow(id);
        Party party = partyService.getPartyOrThrow(partyId);
        if (party.isDeleted() || party.isClosed())
            throw new IllegalArgumentException("존재하지 않거나 삭제된 팟입니다.");

        if (yakiRepositoy.existsYakiByPartyAndUser(party, user))
            throw new IllegalArgumentException("이미 신청한 팟입니다.");
        yakiRepositoy.save(new Yaki(user, party));
    }

    @Transactional
    public void cancelApplication(Long id, Long partyId){
        User user = userService.getUserOrThrow(id);
        Party party = partyService.getPartyOrThrow(partyId);
        if (party.isDeleted() && party.isClosed())
            throw new IllegalArgumentException("존재하지 않거나 삭제된 팟입니다.");

        Yaki yaki = getYakiOrThrow(party, user);
        if (yaki.getStatus() != YakiStatus.WAITING)
            throw new IllegalArgumentException("신청 취소는 대기 중에만 할 수 있습니다.");
        yakiRepositoy.delete(yaki);
    }

    @Transactional(readOnly = true)
    public Yaki getYakiOrThrow(Party party, User user){
        return yakiRepositoy.findYakiByPartyAndUser(party, user).orElseThrow(() -> new IllegalArgumentException("신청 내역이 없습니다."));
    }


}
