package com.sns.api.friends.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.dto.FriendsResponseDto;
import com.sns.api.friends.service.FriendsService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    @PostMapping("/requests")
    public BaseResponse<FriendsResponseDto> requestFriend(SendFriendsRequestDto requestDto, @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {
        return BaseResponse.success(friendsService.requestFriend(requestDto, userBaseDto), ResultCode.CREATED);
    }
}
