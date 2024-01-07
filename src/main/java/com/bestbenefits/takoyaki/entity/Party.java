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
    private User user; //타코

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
    private int recruitNumber; //모집 인원

    @Column
    private LocalDate plannedClosingDate; //마감 날짜

    @Column
    private LocalDate plannedStartDate; //활동 시작 날짜

    @Column
    private int activityDuration; //활동 기간

    @Column
    private String contact;

    @Column
    private Long viewCount;

    @Column
    private LocalDateTime deletedAt; //삭제 일시

    @Column
    private LocalDateTime closedAt; //마감 일시

    @Column
    private LocalDateTime createdAt; //글 작성 일시

    @Column
    private LocalDateTime modifiedAt; //글 수정 일시

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


    /**** 조건 검사  ****/
    //삭제된 파티인지 확인
    public boolean isDeleted() {
        return deletedAt != null;
    }

    //마감된 파티인지 확인
    public boolean isClosed() {
        return !closedAt.isEqual(createdAt);
    }

    //글 작성자인지 확인
    public boolean isAuthor(Long userId) {
        return user.getId().equals(userId);
    }



    /**** 필드 업데이트  ****/
    //팟 수정 시 호출 필요
    public Party updateModifiedAt() {
        this.modifiedAt = LocalDateTime.now();
        return this;
    }


    //팟 삭제에서 사용됨
    public void updateDeleteAt() {
        this.deletedAt = LocalDateTime.now();
    }


    //팟 마감에서 사용됨
    public void updateClosedAt() {
        this.closedAt = LocalDateTime.now();
    }


    //팟 수정에서 사용됨
    public Party updateActivityLocation(ActivityLocation activityLocation) {
        this.activityLocation = activityLocation;
        return this;
    }

    public Party updateContactMethod(ContactMethod contactMethod) {
        this.contactMethod = contactMethod;
        return this;
    }

    public Party updateTitle(String title) {
        this.title = title;
        return this;
    }

    public Party updateBody(String body) {
        this.body = body;
        return this;
    }

    public Party updateRecruitNumber(int recruitNumber) {
        this.recruitNumber = recruitNumber;
        return this;
    }

    public Party updatePlannedClosingDate(LocalDate plannedClosingDate) {
        this.plannedClosingDate = plannedClosingDate;
        return this;
    }

    public Party updatePlannedStartDate(LocalDate plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
        return this;
    }

    public Party updateActivityDuration(int activityDuration) {
        this.activityDuration = activityDuration;
        return this;
    }

    public Party updateContact(String contact) {
        this.contact = contact;
        return this;
    }
}
