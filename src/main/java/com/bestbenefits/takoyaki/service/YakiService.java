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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class YakiService {
    private final UserService userService;
    private final PartyService partyService;
    private final YakiRepositoy yakiRepositoy;

    @Transactional
    public void applyToParty(Long id, Long partyId){
        User user = userService.getUserOrThrow(id);
        Party party = partyService.getPartyOrThrow(partyId);
        if (party.isDeleted() || party.isClosed())
            throw new IllegalArgumentException("존재하지 않거나 삭제된 팟입니다.");

        if (party.getUser() == user)
            throw new IllegalArgumentException("타코는 신청할 수 없습니다.");
        if (yakiRepositoy.existsYakiByPartyAndUser(party, user))
            throw new IllegalArgumentException("이미 신청한 팟입니다.");
        yakiRepositoy.save(new Yaki(user, party));
    }

    @Transactional
    public void cancelApplication(Long id, Long partyId){
        User user = userService.getUserOrThrow(id);
        Party party = partyService.getPartyOrThrow(partyId);
        if (party.isDeleted() || party.isClosed())
            throw new IllegalArgumentException("존재하지 않거나 삭제된 팟입니다.");

        Yaki yaki = getYakiOrThrow(party, user);
        if (yaki.getStatus() != YakiStatus.WAITING)
            throw new IllegalArgumentException("신청 취소는 대기 중에만 할 수 있습니다.");
        yakiRepositoy.delete(yaki);
    }

    @Transactional
    public void acceptYaki(Long id, Long partyId, Long yakiId){
        User user = userService.getUserOrThrow(id); //TODO: boolean으로 확인
        Party party = partyService.getPartyOrThrow(partyId);
        if (party.isDeleted() || party.isClosed())
            throw new IllegalArgumentException("존재하지 않거나 삭제된 팟입니다.");

        Yaki yaki = getYakiOrThrow(yakiId);
        if (yaki.getStatus() != YakiStatus.WAITING)
            throw new IllegalArgumentException("대기중인 야끼만 수락할 수 있습니다.");
        yaki.updateStatus(YakiStatus.ACCEPTED);

        //자리 꽉차면 마감 처리
        if (party.getRecruitNumber() == yakiRepositoy.countByPartyAndStatus(party, YakiStatus.ACCEPTED))
            party.updateClosedAt();
    }



    @Transactional(readOnly = true)
    public Yaki getYakiOrThrow(Party party, User user){
        return yakiRepositoy.findYakiByPartyAndUser(party, user).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 야끼입니다."));
    }

    @Transactional(readOnly = true)
    public Yaki getYakiOrThrow(Long yakiId){
        return yakiRepositoy.findYakiById(yakiId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 야끼입니다."));
    }

}
