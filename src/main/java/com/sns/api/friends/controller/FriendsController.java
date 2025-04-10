package com.sns.api.friends.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.dto.request.ActionFriendsRequestDto;
import com.sns.api.friends.domain.dto.request.SendFriendsRequestDto;
import com.sns.api.friends.domain.dto.response.CommonFriendsResponseDto;
import com.sns.api.friends.domain.dto.response.FindFriendsResponseDto;
import com.sns.api.friends.service.FriendsService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    /**
     * 내 친구 전체 조회 API
     *
     * @param userBaseDto 로그인 유저 정보
     * @param pageable 페이징 정보(기본 5개씩 출력)
     * @return 조회된 친구 Page<DTO> 및 200 응답
     */
    @GetMapping("/me")
    public BaseResponse<Page<FindFriendsResponseDto>> findAcceptFriends(@SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto,
                                                                        @PageableDefault(size = 5) Pageable pageable) {
        return BaseResponse.success(friendsService.findAcceptFriends(userBaseDto, pageable), ResultCode.OK);
    }

    /**
     * 내가 받은 친구 요청 전체 조회 API
     *
     * @param userBaseDto 로그인 유저 정보
     * @param pageable 페이징 정보(기본 5개씩 출력)
     * @return 조회된 받은 친구 요청 Page<DTO> 및 200 응답
     */
    @GetMapping("/request/received")
    public BaseResponse<Page<FindFriendsResponseDto>> findReceivedFriends(@SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto,
                                                                        @PageableDefault(size = 5) Pageable pageable) {
        return BaseResponse.success(friendsService.findReceivedFriends(userBaseDto, pageable), ResultCode.OK);
    }

    /**
     * 내가 보낸 친구 요청 전체 조회 API
     *
     * @param userBaseDto 로그인 유저 정보
     * @param pageable 페이징 정보(기본 5개씩 출력)
     * @return 조회된 보낸 친구 요청 Page<DTO> 및 200 응답
     */
    @GetMapping("/request/sent")
    public BaseResponse<Page<FindFriendsResponseDto>> findSentFriends(@SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto,
                                                                        @PageableDefault(size = 5) Pageable pageable) {
        return BaseResponse.success(friendsService.findSentFriends(userBaseDto, pageable), ResultCode.OK);
    }

    /**
     * 친구 요청 API
     *
     * @param requestDto  요청 받은 UserId
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
     * @param requestId   요청한 Friends 테이블의 PK 값
     * @param requestDto  요청 상태값
     * @param userBaseDto 로그인 유저 정보
     * @return 수락 시 응답 DTO 및 200 응답, 거절, 취소 시 null 및 204 응답
     */
    @PutMapping("/requests/{requestId}")
    public BaseResponse<CommonFriendsResponseDto> actionFriend(@PathVariable Long requestId,
                                                               @RequestBody @Valid ActionFriendsRequestDto requestDto,
                                                               @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {

        CommonFriendsResponseDto commonFriendsResponseDto = friendsService.actionFriend(requestId, requestDto, userBaseDto);
        if (commonFriendsResponseDto == null) {
            return BaseResponse.success(null, ResultCode.NO_CONTENT);
        }

        return BaseResponse.success(commonFriendsResponseDto, ResultCode.OK);
    }

    /**
     * 친구 삭제 API(ACCEPT)
     *
     * @param requestId 삭제할 대상 PK
     * @param userBaseDto 로그인한 유저
     * @return 204 응답
     */
    @DeleteMapping("/{requestId}")
    public BaseResponse<Void> deleteFriends(@PathVariable Long requestId,
                                            @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {
        friendsService.deleteFriends(requestId, userBaseDto);
        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }

}
