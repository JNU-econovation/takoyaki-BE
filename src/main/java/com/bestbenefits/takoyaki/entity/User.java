package com.bestbenefits.takoyaki.entity;

import com.bestbenefits.takoyaki.config.properties.oauth.OAuthSocialType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 16, unique = true)
    private String nickname;

    @Column(length = 100, nullable = false)
    private String email;

    @Column
    @Enumerated(value = EnumType.STRING)
    private OAuthSocialType social;

    @Column(nullable = false)
    private LocalDate nicknameUpdatedAt;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public User(String email, OAuthSocialType social){
        this.email = email;
        this.social = social;
        this.createdAt = LocalDateTime.now();
        this.nicknameUpdatedAt = LocalDate.now().minusDays(1);
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
    public void updateNicknameUpdatedAt(){
        this.nicknameUpdatedAt = LocalDate.now();
    }

}
