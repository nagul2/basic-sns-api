package com.sns.api.friends.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.dto.request.SendFriendsRequestDto;
import com.sns.api.friends.domain.dto.response.SendFriendsResponseDto;
import com.sns.api.friends.service.FriendsService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    @PostMapping("/requests")
    public BaseResponse<SendFriendsResponseDto> requestFriend(@RequestBody @Valid SendFriendsRequestDto requestDto,
                                                              @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {
        return BaseResponse.success(friendsService.requestFriend(requestDto, userBaseDto), ResultCode.CREATED);
    }
}
