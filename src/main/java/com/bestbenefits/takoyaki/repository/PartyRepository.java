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
    Page<Object[]> getPartiesByFilteringAndPagination(Pageable pageable, @Nullable User user, Category category, ActivityLocation activityLocation);

    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate," +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber," +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber, " +
            "CASE WHEN b.user IS NOT NULL THEN true ELSE false END AS bookmarked " +
            "FROM Party p " +
            "LEFT JOIN Bookmark b ON b.party = p AND b.user = :user " +
            "WHERE p.deletedAt IS NULL " +
            "AND p.createdAt = p.closedAt "+
            "AND EXISTS (SELECT 1 FROM Yaki y WHERE y.party = p AND y.user = :user AND y.status = :status)" +
            "ORDER BY p.id DESC")
    List<Object[]> getNotClosedParties(User user, YakiStatus status);

    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate," +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber," +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber " +
            "FROM Party p " +
            "WHERE p.deletedAt IS NULL " +
            "AND p.createdAt != p.closedAt "+
            "AND EXISTS (SELECT 1 FROM Yaki y WHERE y.party = p AND y.user = :user AND y.status = 'ACCEPTED')" +
            "ORDER BY p.id DESC")
    List<Object[]> getClosedParties(User user);

    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate," +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber," +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber " +
            "FROM Party p " +
            "INNER JOIN Bookmark b ON b.party = p AND b.user = :user " +
            "WHERE p.deletedAt IS NULL " +
            "ORDER BY p.id DESC")
    List<Object[]> getBookmarkedParties(User user);
    @Query("SELECT p.id, p.title, p.category, p.activityLocation, p.recruitNumber, p.plannedClosingDate, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'WAITING') AS waitingNumber, " +
            "(SELECT COUNT(*) FROM Yaki y WHERE y.party = p AND y.status = 'ACCEPTED') AS acceptedNumber " +
            "FROM Party p " +
            "WHERE p.deletedAt IS NULL " +
            "AND p.user = :user " +
            "ORDER BY p.id DESC")
    List<Object[]> getWroteParties(User user);
}
