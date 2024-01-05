package com.bestbenefits.takoyaki.entity;

import com.bestbenefits.takoyaki.config.properties.yaki.YakiStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Yaki {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Party party;

    @Column
    @Enumerated(EnumType.STRING)
    private YakiStatus yakiStatus;

    @Column
    private LocalDateTime appliedAt;

    @Builder
    public Yaki(Long id, User user, Party party) {
        this.user = user;
        this.party = party;
        this.yakiStatus = YakiStatus.ACCEPTED;
        this.appliedAt = LocalDateTime.now();
    }
}
