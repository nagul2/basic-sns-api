package com.sns.api.likes.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.likes.domain.dto.LikeResponseDto;
import com.sns.api.likes.domain.entity.LikeType;
import com.sns.api.likes.service.LikesService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class PostLikesController {

    private final LikesService likesService;

    @PostMapping
    public BaseResponse<LikeResponseDto> likePost(@PathVariable Long postId,
                                                  @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {

        return BaseResponse.success(likesService.createLike(postId, LikeType.POST, userBaseDto), ResultCode.CREATED);
    }
}
