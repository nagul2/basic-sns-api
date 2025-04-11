package com.sns.api.likes.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.likes.domain.dto.LikeCountResponseDto;
import com.sns.api.likes.domain.dto.LikeResponseDto;
import com.sns.api.likes.domain.entity.LikeType;
import com.sns.api.likes.service.LikesService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/comments/{commentId}/likes")
@RequiredArgsConstructor
public class CommentLikesController {

    private final LikesService likesService;

    /**
     * 댓글 좋아요 요청
     *
     * @param commentId   댓글 PK
     * @param userBaseDto 로그인 유저 정보
     * @return 성공 시 LikeResponseDto 및 201 응답
     */
    @PostMapping
    public BaseResponse<LikeResponseDto> likeComment(@PathVariable Long commentId,
                                                     @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {

        return BaseResponse.success(likesService.createLike(commentId, LikeType.COMMENT, userBaseDto),
                ResultCode.CREATED);
    }

    /**
     * 댓글 좋아요 취소
     *
     * @param commentId   댓글 PK
     * @param userBaseDto 로그인 유저 정보
     * @return 성공 시  204 응답
     */
    @DeleteMapping
    public BaseResponse<Void> unlikeComment(@PathVariable Long commentId,
                                            @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {

        likesService.deleteLike(commentId, LikeType.COMMENT, userBaseDto);
        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }

    /**
     * 댓글 좋아요 수 조회
     *
     * @param commentId 댓글 PK
     * @return 성공 시 LikeCountResponseDto 및 200 응답
     */
    @GetMapping("/count")
    public BaseResponse<LikeCountResponseDto> countCommentLike(@PathVariable Long commentId) {

        return BaseResponse.success(likesService.countLike(commentId, LikeType.COMMENT), ResultCode.OK);
    }
}
