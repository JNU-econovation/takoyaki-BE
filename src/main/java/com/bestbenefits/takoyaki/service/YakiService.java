package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.config.properties.user.YakiStatus;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.entity.Yaki;
import com.bestbenefits.takoyaki.repository.YakiRepositoy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class YakiService {
    private final UserService userService;
    private final PartyService partyService;
    private final YakiRepositoy yakiRepository;

    @Transactional
    public void applyToParty(Long id, Long partyId){
        User user = userService.getUserOrThrow(id);
        Party party = partyService.getPartyOrThrow(partyId);
        if (party.isDeleted() || party.isClosed())
            throw new IllegalArgumentException("존재하지 않거나 삭제된 팟입니다.");

        if (party.getUser() == user)
            throw new IllegalArgumentException("타코는 신청할 수 없습니다.");
        if (yakiRepository.existsYakiByPartyAndUser(party, user))
            throw new IllegalArgumentException("이미 신청한 팟입니다.");
        yakiRepository.save(new Yaki(user, party));
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
        yakiRepository.delete(yaki);
    }

    @Transactional
    public void acceptYaki(Long id, Long partyId, Long yakiId){
        User yakiUser = userService.getUserOrThrow(yakiId);
        Party party = partyService.getPartyOrThrow(partyId);
        if (party.isDeleted() || party.isClosed())
            throw new IllegalArgumentException("존재하지 않거나 삭제된 팟입니다.");

        if (!party.isAuthor(id))
            throw new IllegalArgumentException("타코만 요청을 수락할 수 있습니다.");
        Yaki yaki = getYakiOrThrow(party, yakiUser);
        if (yaki.getStatus() != YakiStatus.WAITING)
            throw new IllegalArgumentException("대기중인 야끼만 수락할 수 있습니다.");
        yaki.updateStatus(YakiStatus.ACCEPTED);

        //TODO: 팟 마감과 합칠 수 있으면 합치기
        if (party.getRecruitNumber() == yakiRepository.countByPartyAndStatus(party, YakiStatus.ACCEPTED))
            party.updateClosedAt();
    }

    @Transactional
    public void denyYaki(Long id, Long partyId, Long yakiId){
        User yakiUser = userService.getUserOrThrow(yakiId);
        Party party = partyService.getPartyOrThrow(partyId);
        if (party.isDeleted() || party.isClosed())
            throw new IllegalArgumentException("존재하지 않거나 삭제된 팟입니다.");

        if (!party.isAuthor(id))
            throw new IllegalArgumentException("타코만 요청을 거절할 수 있습니다.");
        Yaki yaki = getYakiOrThrow(party, yakiUser);
        if (yaki.getStatus() != YakiStatus.WAITING)
            throw new IllegalArgumentException("대기중인 야끼만 거절할 수 있습니다.");
        yakiRepository.delete(yaki);
    }


    @Transactional
    public void leaveParty(Long id, Long partyId){
        User user = userService.getUserOrThrow(id);
        Party party = partyService.getPartyOrThrow(partyId);
        if (party.isDeleted() || party.isClosed())
            throw new IllegalArgumentException("존재하지 않거나 삭제된 팟입니다.");

        Yaki yaki = getYakiOrThrow(party, user);
        if (yaki.getStatus() != YakiStatus.ACCEPTED)
            throw new IllegalArgumentException("수락된 상태가 아닙니다.");
        yakiRepository.delete(yaki);
    }





    @Transactional(readOnly = true)
    public Yaki getYakiOrThrow(Party party, User user){
        return yakiRepository.findYakiByPartyAndUser(party, user).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 야끼입니다."));
    }

}
