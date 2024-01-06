package com.bestbenefits.takoyaki.repository;

import com.bestbenefits.takoyaki.DTO.client.response.PartyListForLoginUserResDTO;
import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber " +
            "FROM Party p " +
            "WHERE p.deletedAt IS NULL " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND (:activityLocation IS NULL OR p.activityLocation = :activityLocation)" +
            "ORDER BY p.id DESC")
    Page<Object[]> getPartiesByFiltering(Pageable pageable, Category category, ActivityLocation activityLocation);

    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber, " +
            "CASE WHEN b.user IS NOT NULL THEN true ELSE false END AS bookmarked " +
            "FROM Party p " +
            "LEFT JOIN Bookmark b ON b.party = p AND b.user = :user " +
            "WHERE p.deletedAt IS NULL " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND (:activityLocation IS NULL OR p.activityLocation = :activityLocation)" +
            "ORDER BY p.id DESC")
    Page<Object[]> getPartiesWithBookmarkFlagByFiltering(User user, Pageable pageable, Category category, ActivityLocation activityLocation);
}
