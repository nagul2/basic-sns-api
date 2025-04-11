package com.sns.api.comments.controller;

import com.sns.api.comments.domain.dto.request.CommentCreateRequestDto;
import com.sns.api.comments.domain.dto.request.CommentUpdateRequestDto;
import com.sns.api.comments.domain.dto.response.CommentResponseDto;
import com.sns.api.comments.service.CommentsService;
import com.sns.api.common.domain.dto.PageResponseDto;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    /**
     * 특정 게시글의 댓글 등록 API
     *
     * @param postId           요청한 Posts 테이블의 PK 값
     * @param createRequestDto 댓글 저장 데이터
     * @return 성공 시 CommentResponseDto 및 201 응답
     */
    @PostMapping
    public BaseResponse<CommentResponseDto> createComment(@PathVariable Long postId,
                                                          @RequestBody @Valid CommentCreateRequestDto createRequestDto) {

        CommentResponseDto savedComment = commentsService.createComment(postId, createRequestDto);

        return BaseResponse.success(savedComment, ResultCode.CREATED);
    }

    /**
     * 특정 게시글의 특정 댓글 조회 API
     *
     * @param postId    요청한 Posts 테이블의 PK 값
     * @param commentId 요청한 Comments 테이블의 PK 값
     * @return 성공 시 CommentResponseDto 및 200 응답
     */
    @GetMapping("/{commentId}")
    public BaseResponse<CommentResponseDto> getComment(@PathVariable Long postId, @PathVariable Long commentId) {

        CommentResponseDto comment = commentsService.getCommentById(postId, commentId);

        return BaseResponse.success(comment, ResultCode.OK);
    }

    /**
     * 특정 게시글의 댓글 목록 조회 API
     *
     * @param postId   요청한 Posts 테이블의 PK 값
     * @param pageable 페이징 정보
     * @return 성공 시 Page된 CommentResponseDto 및 200 응답
     */
    @GetMapping
    public BaseResponse<PageResponseDto<CommentResponseDto>> getComments(@PathVariable Long postId,
                                                                         @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                         @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto) {

        PageResponseDto<CommentResponseDto> comments = commentsService.getCommentsByPost(postId, userBaseDto, pageable);

        return BaseResponse.success(comments, ResultCode.OK);
    }

    /**
     * 특정 게시글의 댓글 수정 API
     *
     * @param postId      요청한 Posts 테이블의 PK 값
     * @param commentId   요청한 Comments 테이블의 PK 값
     * @param userBaseDto 로그인 된 유저 정보
     * @return 성공 시 CommentResponseDto 및 200 응답
     */
    @PutMapping("/{commentId}")
    public BaseResponse<CommentResponseDto> updateComment(@PathVariable Long postId,
                                                          @PathVariable Long commentId,
                                                          @RequestBody @Valid CommentUpdateRequestDto updateRequestDto,
                                                          @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto) {

        CommentResponseDto updatedComment = commentsService.updateComment(postId, commentId, updateRequestDto, userBaseDto);

        return BaseResponse.success(updatedComment, ResultCode.OK);
    }

    /**
     * 특정 게시글의 댓글 삭제 API
     *
     * @param postId      요청한 Posts 테이블의 PK 값
     * @param commentId   요청한 Comments 테이블의 PK 값
     * @param userBaseDto 로그인 된 유저 정보
     * @return 성공 시 204 응답
     */
    @DeleteMapping("/{commentId}")
    public BaseResponse<Void> deleteComment(@PathVariable Long postId,
                                            @PathVariable Long commentId,
                                            @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto) {

        commentsService.deleteComment(postId, commentId, userBaseDto);

        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }

}

