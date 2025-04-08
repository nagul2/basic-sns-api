package com.sns.api.friends.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendFriendsRequestDto {

    private final Long receiverId;
}
