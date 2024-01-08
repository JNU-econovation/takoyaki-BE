package com.bestbenefits.takoyaki.entity;

import com.bestbenefits.takoyaki.config.properties.auth.OAuthSocialType;
import jakarta.validation.constraints.Size;
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

    @Column(unique = true) // nullable = false
    @Size(min=4, max=16)
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
//        this.nickname = ;
        LocalDateTime timestamp = LocalDateTime.now();
        this.email = email;
        this.social = social;
        this.createdAt = timestamp;
        this.nicknameUpdatedAt = timestamp.toLocalDate().minusDays(1);
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
    public void updateNicknameUpdatedAt(){
        this.nicknameUpdatedAt = LocalDate.now();
    }

}
