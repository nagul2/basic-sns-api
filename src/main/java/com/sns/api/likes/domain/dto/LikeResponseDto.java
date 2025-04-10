package com.sns.api.likes.domain.dto;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.likes.domain.entity.LikeType;
import com.sns.api.likes.domain.entity.Likes;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LikeResponseDto {

    private Long likeTypeId;

    private LikeType likeType;

    private UserBaseDto likedBy;

    private LocalDateTime createdAt;

    public static LikeResponseDto fromEntity(Likes likes) {
        return LikeResponseDto.builder()
                .likeTypeId(likes.getLikeTypeId())
                .likeType(likes.getLikeType())
                .likedBy(UserBaseDto.fromEntity(likes.getCreatedBy()))
                .createdAt(likes.getCreatedAt())
                .build();

    }
}
