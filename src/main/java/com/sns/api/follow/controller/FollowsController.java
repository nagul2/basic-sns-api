package com.sns.api.follow.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.follow.domain.dto.request.FollowsRequestDto;
import com.sns.api.follow.domain.dto.response.FollowsResponseDto;
import com.sns.api.follow.service.FollowsService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowsController {

    private final FollowsService followsService;

    @PostMapping
    public BaseResponse<FollowsResponseDto> follow(@RequestBody @Valid FollowsRequestDto requestDto
            , @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {
        return BaseResponse.success(followsService.follow(requestDto, userBaseDto), ResultCode.CREATED);
    }

    @DeleteMapping("/{followId}")
    public BaseResponse<Void> unFollow(@PathVariable Long followId, @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {
        followsService.unFollow(followId, userBaseDto);
        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }
}