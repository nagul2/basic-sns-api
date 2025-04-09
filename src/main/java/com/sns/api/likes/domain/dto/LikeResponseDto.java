package com.sns.api.likes.domain.dto;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.likes.domain.entity.Likes;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LikeResponseDto {

    private Long postId;

    private UserBaseDto likedBy;

    private LocalDateTime createdAt;

    public static LikeResponseDto fromEntity(Likes likes) {
        return LikeResponseDto.builder()
                .postId(likes.getLikeTypeId())
                .likedBy(UserBaseDto.fromEntity(likes.getCreatedBy()))
                .createdAt(likes.getCreatedAt())
                .build();

    }
}
