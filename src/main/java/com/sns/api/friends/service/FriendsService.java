package com.sns.api.friends.service;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.dto.request.ActionFriendsRequestDto;
import com.sns.api.friends.domain.dto.request.SendFriendsRequestDto;
import com.sns.api.friends.domain.dto.response.CommonFriendsResponseDto;

public interface FriendsService {
    CommonFriendsResponseDto requestFriend(SendFriendsRequestDto requestDto, UserBaseDto userBaseDto);

    CommonFriendsResponseDto actionFriend(Long friendsId, ActionFriendsRequestDto requestDto, UserBaseDto userBaseDto);

    void deleteFriends(Long requestId, UserBaseDto userBaseDto);
}
