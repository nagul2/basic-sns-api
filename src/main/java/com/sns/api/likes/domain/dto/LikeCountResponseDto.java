package com.sns.api.likes.domain.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LikeCountResponseDto {

    private Long postId;
    private Long likeCount;

    public static LikeCountResponseDto fromEntity(Long postId, Long likeCount) {
        return LikeCountResponseDto.builder()
                .postId(postId)
                .likeCount(likeCount)
                .build();
    }
}
