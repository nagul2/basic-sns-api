package com.sns.api.follow.service;

import com.sns.api.common.domain.dto.PageResponseDto;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.follow.domain.dto.request.FollowsRequestDto;
import com.sns.api.follow.domain.dto.response.FollowsResponseDto;
import org.springframework.data.domain.Pageable;

public interface FollowsService {

    FollowsResponseDto follow(FollowsRequestDto requestDto, UserBaseDto userBaseDto);

    void unFollow(Long followId, UserBaseDto userBaseDto);

    PageResponseDto<FollowsResponseDto> getFollowers(UserBaseDto userBaseDto, Pageable pageable);

    PageResponseDto<FollowsResponseDto> getFollowings(UserBaseDto userBaseDto, Pageable pageable);
}
