package com.bestbenefits.takoyaki.repository;

import com.bestbenefits.takoyaki.entity.Bookmark;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> getBookmarksByUser(User user);

    Optional<Bookmark> findByUserAndParty(User user, Party party);

    void deleteAllByParty(Party party);
}
