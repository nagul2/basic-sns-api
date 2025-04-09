package com.sns.api.friends.domain.dto.response;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.entity.Friends;
import com.sns.api.friends.domain.entity.FriendsStatus;
import com.sns.api.users.domain.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FindFriendsResponseDto {

    private final Long requestId;
    private final UserBaseDto friend;
    private final FriendsStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static FindFriendsResponseDto toMapDto(Friends friends, Users friend) {
        return new FindFriendsResponseDto(
                friends.getId(),
                UserBaseDto.fromEntity(friend),
                friends.getStatus(),
                friends.getCreatedAt(),
                friends.getModifiedAt()
        );
    }

}
