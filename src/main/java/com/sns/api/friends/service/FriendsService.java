package com.sns.api.friends.service;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.dto.request.SendFriendsRequestDto;
import com.sns.api.friends.domain.dto.response.SendFriendsResponseDto;

public interface FriendsService {
    SendFriendsResponseDto requestFriend(SendFriendsRequestDto requestDto, UserBaseDto userBaseDto);
}
