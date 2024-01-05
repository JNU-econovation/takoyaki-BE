package com.bestbenefits.takoyaki.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {
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
    private LocalDateTime createdAt;

    @Builder

    public Bookmark(User user, Party party) {
        this.user = user;
        this.party = party;
        this.createdAt = LocalDateTime.now();
    }
}
