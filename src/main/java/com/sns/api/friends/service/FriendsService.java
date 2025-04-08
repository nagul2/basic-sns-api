package com.sns.api.friends.service;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.controller.SendFriendsRequestDto;
import com.sns.api.friends.domain.dto.response.FriendsResponseDto;

public interface FriendsService {
    FriendsResponseDto requestFriend(SendFriendsRequestDto requestDto, UserBaseDto userBaseDto);
}
