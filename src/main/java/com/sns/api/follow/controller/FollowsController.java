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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowsController {

    private final FollowsService followsService;

    /**
     * 팔로우 API
     *
     * @param requestDto  팔로우 대상 UserId
     * @param userBaseDto 요청 한 User Id
     * @return 성공 시 DTO 및 201 응답
     */
    @PostMapping
    public BaseResponse<FollowsResponseDto> follow(@RequestBody @Valid FollowsRequestDto requestDto
            , @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {
        return BaseResponse.success(followsService.follow(requestDto, userBaseDto), ResultCode.CREATED);
    }

    /**
     * 팔로우 취소
     *
     * @param followId    요청한 follows 테이블의 PK 값
     * @param userBaseDto 로그인 유저 정보
     *                    팔로잉, 팔로워 둘다 속하지 않으면 exception
     * @return 204 응답
     */

    @DeleteMapping("/{followId}")
    public BaseResponse<Void> unFollow(@PathVariable Long followId, @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {
        followsService.unFollow(followId, userBaseDto);
        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }

    /**
     * 나의 팔로워 목록 조회 API
     *
     * @param userBaseDto 로그인 유저 정보
     * @param pageable    페이징 정보(기본 5개씩 출력)
     * @return 조회된 팔로워 Page<DTO> 및 200 응답
     */
    @GetMapping("/followers")
    public BaseResponse<Page<FollowsResponseDto>> getMyFollowers(@SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto,
                                                                 @PageableDefault(size = 5) Pageable pageable) {
        return BaseResponse.success(followsService.getFollowers(userBaseDto, pageable), ResultCode.OK);
    }

    /**
     * 나의 팔로잉 목록 조회 API
     *
     * @param userBaseDto 로그인 유저 정보
     * @param pageable    페이징 정보(기본 5개씩 출력)
     * @return 조회된 팔로잉 Page<DTO> 및 200 응답
     */
    @GetMapping("/followings")
    public BaseResponse<Page<FollowsResponseDto>> getMyFollowings(@SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto,
                                                                  @PageableDefault(size = 5) Pageable pageable) {
        return BaseResponse.success(followsService.getFollowings(userBaseDto, pageable), ResultCode.OK);
    }
}