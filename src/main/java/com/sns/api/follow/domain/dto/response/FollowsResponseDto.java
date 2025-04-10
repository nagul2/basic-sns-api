package com.sns.api.follow.domain.dto.response;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.follow.domain.entity.Follows;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FollowsResponseDto {

    private Long followId;

    private UserBaseDto follower;

    private UserBaseDto following;

    private LocalDateTime createdAt;

    public static FollowsResponseDto fromEntity(Follows entity) {
        return new FollowsResponseDto(
                entity.getId(),
                UserBaseDto.fromEntity(entity.getFollower()),
                UserBaseDto.fromEntity(entity.getFollowing()),
                entity.getCreatedAt()
        );
    }
}
