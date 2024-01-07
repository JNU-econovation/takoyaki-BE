package com.bestbenefits.takoyaki.repository;

import com.bestbenefits.takoyaki.DTO.client.response.PartyYakiListResDTO;
import com.bestbenefits.takoyaki.config.properties.user.YakiStatus;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.entity.Yaki;
import com.bestbenefits.takoyaki.service.YakiService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YakiRepositoy extends JpaRepository<Yaki, Long> {
    Optional<Yaki> findYakiByUser(User user);
    Optional<Yaki> findYakiByPartyAndUser(Party party, User user);

    @Query("SELECT new com.bestbenefits.takoyaki.DTO.client.response.PartyYakiListResDTO(y.user.id, y.user.nickname) FROM Yaki y WHERE y.party = :party AND y.status = 'WAITING'")
    List<PartyYakiListResDTO> findWaitingList(Party party);

    @Query("SELECT new com.bestbenefits.takoyaki.DTO.client.response.PartyYakiListResDTO(y.user.id, y.user.nickname) FROM Yaki y WHERE y.party = :party AND y.status = 'ACCEPTED'")
    List<PartyYakiListResDTO> findAcceptedList(Party party);

    boolean existsYakiByPartyAndUser(Party party, User user);

    long countByPartyAndStatus(Party party, YakiStatus status);

}
