package com.sns.api.friends.domain.dto.response;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.entity.Friends;
import com.sns.api.friends.domain.entity.FriendsStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommonFriendsResponseDto {

    private final Long requestId;
    private final UserBaseDto sender;
    private final UserBaseDto receiver;
    private final FriendsStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static CommonFriendsResponseDto toMapDto(Friends friends) {
        return new CommonFriendsResponseDto(
                friends.getId(),
                UserBaseDto.fromEntity(friends.getFromUser()),
                UserBaseDto.fromEntity(friends.getToUser()),
                friends.getStatus(),
                friends.getCreatedAt(),
                friends.getModifiedAt()
        );

    }
}
