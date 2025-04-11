package com.sns.api.posts.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import com.sns.api.posts.service.PostsService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/users/me/likes")
@RequiredArgsConstructor
public class LikedPostController {

    private final PostsService postsService;

    @GetMapping
    public BaseResponse<Page<PostResponseDto>> getMyLikedPosts(
            @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto,
            @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

        return BaseResponse.success(postsService.getMyLikedPosts(userBaseDto, pageable), ResultCode.OK);
    }
}

