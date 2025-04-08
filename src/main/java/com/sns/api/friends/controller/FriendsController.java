package com.sns.api.friends.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.dto.request.ActionFriendsRequestDto;
import com.sns.api.friends.domain.dto.request.SendFriendsRequestDto;
import com.sns.api.friends.domain.dto.response.CommonFriendsResponseDto;
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

    /**
     * 친구 요청 API
     *
     * @param requestDto 요청 받은 UserId
     * @param userBaseDto 요청 한 User Id
     * @return 성공 시 DTO 및 201 응답
     */
    @PostMapping("/requests")
    public BaseResponse<CommonFriendsResponseDto> requestFriend(@RequestBody @Valid SendFriendsRequestDto requestDto,
                                                                @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {
        return BaseResponse.success(friendsService.requestFriend(requestDto, userBaseDto), ResultCode.CREATED);
    }

    /**
     * 친구 요청 수락/거절/취소 API
     *
     * @param requestId 요청한 Friends 테이블의 PK 값
     * @param requestDto 요청 상태값
     * @param userBaseDto 로그인 유저 정보
     * @return 수락 시 응답 DTO 및 200 응답, 거절, 취소 시 null 및 204 응답
     */
    @PutMapping("/requests/{requestId}")
    public BaseResponse<CommonFriendsResponseDto> actionFriend(@PathVariable Long requestId,
                                                               @RequestBody @Valid ActionFriendsRequestDto requestDto,
                                                               @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {

        CommonFriendsResponseDto CommonFriendsResponseDto = friendsService.actionFriend(requestId, requestDto, userBaseDto);
        if (CommonFriendsResponseDto == null) {
            return BaseResponse.success(null, ResultCode.NO_CONTENT);
        }

        return BaseResponse.success(CommonFriendsResponseDto, ResultCode.OK);
    }
}
