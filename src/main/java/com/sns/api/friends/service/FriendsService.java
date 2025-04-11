package com.sns.api.friends.service;

import com.sns.api.common.domain.dto.PageResponseDto;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.dto.request.ActionFriendsRequestDto;
import com.sns.api.friends.domain.dto.request.SendFriendsRequestDto;
import com.sns.api.friends.domain.dto.response.CommonFriendsResponseDto;
import com.sns.api.friends.domain.dto.response.FindFriendsResponseDto;
import org.springframework.data.domain.Pageable;

public interface FriendsService {
    PageResponseDto<FindFriendsResponseDto> findAcceptFriends(UserBaseDto userBaseDto, Pageable pageable);

    PageResponseDto<FindFriendsResponseDto> findReceivedFriends(UserBaseDto userBaseDto, Pageable pageable);

    PageResponseDto<FindFriendsResponseDto> findSentFriends(UserBaseDto userBaseDto, Pageable pageable);

    CommonFriendsResponseDto requestFriend(SendFriendsRequestDto requestDto, UserBaseDto userBaseDto);

    CommonFriendsResponseDto actionFriend(Long friendsId, ActionFriendsRequestDto requestDto, UserBaseDto userBaseDto);

    void deleteFriends(Long requestId, UserBaseDto userBaseDto);
}
