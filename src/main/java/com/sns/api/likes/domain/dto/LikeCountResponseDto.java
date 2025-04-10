package com.sns.api.likes.domain.dto;

import com.sns.api.likes.domain.entity.LikeType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LikeCountResponseDto {

    private Long likeTypeId;

    private LikeType likeType;

    private Long likeCount;

    public static LikeCountResponseDto fromEntity(Long postId, LikeType likeType, Long likeCount) {
        return LikeCountResponseDto.builder()
                .likeTypeId(postId)
                .likeType(likeType)
                .likeCount(likeCount)
                .build();
    }
}
