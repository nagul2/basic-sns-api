package com.sns.api.friends.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.entity.Friends;
import com.sns.api.friends.domain.entity.FriendsStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FriendsResponseDto {

    private final Long requestId;
    private final UserBaseDto sender;
    private final UserBaseDto receiver;
    private final FriendsStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime modifiedAt;


    public static FriendsResponseDto toMapDto(Friends friends) {
        return new FriendsResponseDto(
                friends.getId(),
                UserBaseDto.fromEntity(friends.getFrom()),
                UserBaseDto.fromEntity(friends.getTo()),
                friends.getStatus(),
                friends.getCreatedAt(),
                friends.getModifiedAt()
        );

    }
}
