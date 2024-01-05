package com.bestbenefits.takoyaki.repository;

import com.bestbenefits.takoyaki.config.properties.oauth.OAuthSocialType;
import com.bestbenefits.takoyaki.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);
    Optional<User> findUserByEmailAndSocial(String Email, OAuthSocialType social);
    Optional<User> findUserByNickname(String nickname);
}