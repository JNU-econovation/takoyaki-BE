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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Party party;

    @Column(nullable = false)
    private String body;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public Comment(User user, Party party, String body) {
        this.user = user;
        this.party = party;
        this.body = body;
        this.createdAt = LocalDateTime.now();
    }
}