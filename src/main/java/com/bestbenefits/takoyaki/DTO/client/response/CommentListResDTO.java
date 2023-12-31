package com.bestbenefits.takoyaki.DTO.client.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommentListResDTO(@NotBlank String createdAt, @NotBlank String nickname, @NotBlank String comment, Boolean isAuthor) {}
