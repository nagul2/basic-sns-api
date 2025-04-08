package com.sns.api.friends.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendFriendsRequestDto {

    private final Long receiverId;
}
