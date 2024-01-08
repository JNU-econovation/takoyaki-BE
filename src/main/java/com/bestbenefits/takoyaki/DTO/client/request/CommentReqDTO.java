package com.bestbenefits.takoyaki.DTO.client.request;

import com.bestbenefits.takoyaki.entity.Comment;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommentReqDTO(@NotBlank String comment) {
    public Comment toEntity(User user, Party party) {
        //System.out.println("comment = " + comment);
        return Comment.builder()
                .user(user)
                .party(party)
                .body(comment)
                .build();
    }
}
