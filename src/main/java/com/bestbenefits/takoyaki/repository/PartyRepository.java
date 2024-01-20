package com.bestbenefits.takoyaki.repository;

import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.config.properties.user.YakiStatus;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
    /********  모든 팟 조회  ********/
    //row[10] bookmarked임
    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate, p.viewCount, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber, " +
            "CASE WHEN p.closedAt != p.createdAt THEN p.closedAt END AS closedDate, " +
            "CASE WHEN b.user IS NOT NULL THEN true ELSE false END AS bookmarked " + //로그인 시에만 제공
            "FROM Party p " +
            "LEFT JOIN Bookmark b ON b.party = p AND b.user = :user " +
            "WHERE p.deletedAt IS NULL " +
            "AND p.closedAt = p.createdAt " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND (:activityLocation IS NULL OR p.activityLocation = :activityLocation)" +
            "ORDER BY p.id DESC")
    Page<Object[]> getPartiesByFilteringAndPagination(Pageable pageable, @Nullable User user, Category category, ActivityLocation activityLocation);

    /********  특정 팟 조회  ********/
    //row[10] bookmarked임
    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate, p.viewCount, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber," +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber, " +
            "CASE WHEN p.closedAt != p.createdAt THEN p.closedAt END AS closedDate, " +
            "CASE WHEN b.user IS NOT NULL THEN true ELSE false END AS bookmarked " + //두가지 경우에만 제공
            "FROM Party p " +
            "LEFT JOIN Bookmark b ON b.party = p AND b.user = :user " +
            "WHERE p.deletedAt IS NULL " +
            "AND p.createdAt = p.closedAt "+
            "AND EXISTS (SELECT 1 FROM Yaki y WHERE y.party = p AND y.user = :user AND y.status = :status)" +
            "ORDER BY p.id DESC")
    Page<Object[]> getNotClosedParties(Pageable pageable, User user, YakiStatus status);

    //row[10] 없음
    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate, p.viewCount, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber," +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber, " +
            "CASE WHEN p.closedAt != p.createdAt THEN p.closedAt END AS closedDate " +
            "FROM Party p " +
            "WHERE p.deletedAt IS NULL " +
            "AND p.createdAt != p.closedAt "+
            "AND EXISTS (SELECT 1 FROM Yaki y WHERE y.party = p AND y.user = :user AND y.status = 'ACCEPTED')" +
            "ORDER BY p.id DESC")
    Page<Object[]> getClosedParties(Pageable pageable, User user);

    //row[10] 없음
    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate, p.viewCount, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber," +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber, " +
            "CASE WHEN p.closedAt != p.createdAt THEN p.closedAt END AS closedDate " +
            "FROM Party p " +
            "INNER JOIN Bookmark b ON b.party = p AND b.user = :user " +
            "WHERE p.deletedAt IS NULL " +
            "ORDER BY p.id DESC")
    Page<Object[]> getBookmarkedParties(Pageable pageable, User user);

    //row[10] 없음
    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate, p.viewCount, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber, " +
            "CASE WHEN p.closedAt != p.createdAt THEN p.closedAt END AS closedDate " +
            "FROM Party p " +
            "WHERE p.deletedAt IS NULL " +
            "AND p.user = :user " +
            "ORDER BY p.id DESC")
    Page<Object[]> getWroteParties(Pageable pageable, User user);
}
