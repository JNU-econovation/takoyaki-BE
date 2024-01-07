package com.bestbenefits.takoyaki.entity;

import com.bestbenefits.takoyaki.config.properties.party.ActivityLocation;
import com.bestbenefits.takoyaki.config.properties.party.Category;
import com.bestbenefits.takoyaki.config.properties.party.ContactMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ActivityLocation activityLocation;

    @Enumerated(EnumType.STRING)
    private ContactMethod contactMethod;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private int recruitNumber;

    @Column
    private LocalDate plannedClosingDate;

    @Column
    private LocalDate plannedStartDate;

    @Column
    private int activityDuration;

    @Column
    private String contact;

    @Column
    private Long viewCount;

    @Column
    private LocalDateTime deletedAt;

    @Column
    private LocalDateTime closedAt;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Builder
    public Party(User user, Category category, ActivityLocation activityLocation, ContactMethod contactMethod, String title, String body, int recruitNumber, LocalDate plannedClosingDate, LocalDate plannedStartDate, int activityDuration, String contact) {
        LocalDateTime timestamp = LocalDateTime.now();
        this.user = user;
        this.category = category;
        this.activityLocation = activityLocation;
        this.contactMethod = contactMethod;
        this.title = title;
        this.body = body;
        this.recruitNumber = recruitNumber;
        this.plannedClosingDate = plannedClosingDate;
        this.plannedStartDate = plannedStartDate;
        this.activityDuration = activityDuration;
        this.contact = contact;
        this.closedAt = timestamp;
        this.createdAt = timestamp;
        this.modifiedAt = timestamp;
        this.viewCount = 0L;
        this.deletedAt = null;
    }

    public void updateDeleteAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
    //TODO: 게시글 수정에서 이용할 setter
}
