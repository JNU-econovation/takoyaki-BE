package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.config.properties.user.YakiStatus;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.entity.Yaki;
import com.bestbenefits.takoyaki.exception.party.NotTakoException;
import com.bestbenefits.takoyaki.exception.party.PartyNotFoundException;
import com.bestbenefits.takoyaki.exception.party.PartyClosedException;
import com.bestbenefits.takoyaki.exception.yaki.*;
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

        if (party.isDeleted())
            throw new PartyNotFoundException();
        if (party.isClosed())
            throw new PartyClosedException();

        if (party.isAuthor(id)) //타코가 파티를 신청할 수 없음
            throw new TakoNotAllowedException(); //여기서만 사용됨

        if (yakiRepository.existsYakiByPartyAndUser(party, user)) //이미 신청한 파티임
            throw new AlreadyAppliedException(); //여기서만 사용됨

        yakiRepository.save(new Yaki(user, party));
    }

    @Transactional
    public void cancelApplication(Long id, Long partyId){
        User user = userService.getUserOrThrow(id);
        Party party = partyService.getPartyOrThrow(partyId);

        if (party.isDeleted())
            throw new PartyNotFoundException();
        if (party.isClosed())
            throw new PartyClosedException();

        if (party.isAuthor(id)) //타코가 파티를 신청할 수 없음
            throw new TakoNotAllowedException(); //여기서만 사용됨

        Yaki yaki = getYakiOrThrow(party, user);
        if (yaki.getStatus() != YakiStatus.WAITING)
            throw new CancelApplicationNotAllowedException(); //여기서만 사용됨

        yakiRepository.delete(yaki);
    }

    @Transactional
    public void acceptYaki(Long id, Long partyId, Long yakiId){
        Party party = partyService.getPartyOrThrow(partyId);

        if (party.isDeleted())
            throw new PartyNotFoundException();
        if (party.isClosed())
            throw new PartyClosedException();

        if (!party.isAuthor(id)) //타코가 아님
            throw new NotTakoException();

        User yakiUser = userService.getUserOrThrow(yakiId);
        Yaki yaki = getYakiOrThrow(party, yakiUser); //야끼가 아님

        if (yaki.getStatus() != YakiStatus.WAITING)//이미 수락된 야끼임
            throw new AlreadyAcceptedYakiException(); //공통

        yaki.updateStatus(YakiStatus.ACCEPTED);

        //TODO: 팟 마감과 합칠 수 있으면 합치기
        if (party.getRecruitNumber() == yakiRepository.countByPartyAndStatus(party, YakiStatus.ACCEPTED))
            party.updateModifiedAt().updateClosedAt();
    }

    @Transactional
    public void denyYaki(Long id, Long partyId, Long yakiId){
        Party party = partyService.getPartyOrThrow(partyId);

        if (party.isDeleted())
            throw new PartyNotFoundException();
        if (party.isClosed())
            throw new PartyClosedException();

        if (!party.isAuthor(id)) //타코가 아님
            throw new NotTakoException();

        User yakiUser = userService.getUserOrThrow(yakiId);
        Yaki yaki = getYakiOrThrow(party, yakiUser); //야끼가 아님

        if (yaki.getStatus() != YakiStatus.WAITING) //이미 수락된 야끼임
            throw new AlreadyAcceptedYakiException(); //공통

        yakiRepository.delete(yaki);
    }

    @Transactional
    public void leaveParty(Long id, Long partyId){
        User user = userService.getUserOrThrow(id);
        Party party = partyService.getPartyOrThrow(partyId);

        if (party.isDeleted())
            throw new PartyNotFoundException();
        if (party.isClosed())
            throw new PartyClosedException();

        Yaki yaki = getYakiOrThrow(party, user); //야끼가 아님
        if (yaki.getStatus() != YakiStatus.ACCEPTED) //대기 상태에서 팟 나가기 시도
            throw new LeavePartyNotAllowedException(); //여기서만 사용됨
        yakiRepository.delete(yaki);
    }

    @Transactional(readOnly = true)
    public Yaki getYakiOrThrow(Party party, User user){
        return yakiRepository.findYakiByPartyAndUser(party, user)
                .orElseThrow(NotYakiException::new);
    }
}
