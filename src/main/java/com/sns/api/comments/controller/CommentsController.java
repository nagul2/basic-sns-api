package com.sns.api.comments.controller;

import com.sns.api.comments.domain.dto.request.CommentCreateRequestDto;
import com.sns.api.comments.domain.dto.request.CommentUpdateRequestDto;
import com.sns.api.comments.domain.dto.response.CommentResponseDto;
import com.sns.api.comments.service.CommentsService;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping
    public BaseResponse<CommentResponseDto> createComment(@PathVariable Long postId,
                                                          @RequestBody @Valid CommentCreateRequestDto createRequestDto) {

        CommentResponseDto savedComment = commentsService.createComment(postId, createRequestDto);

        return BaseResponse.success(savedComment, ResultCode.CREATED);
    }

    @GetMapping("/{commentId}")
    public BaseResponse<CommentResponseDto> getComment(@PathVariable Long postId, @PathVariable Long commentId) {

        CommentResponseDto comment = commentsService.getCommentById(postId, commentId);

        return BaseResponse.success(comment, ResultCode.OK);
    }

    @GetMapping
    public BaseResponse<Page<CommentResponseDto>> getComments(@PathVariable Long postId,
                                                              @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CommentResponseDto> comments = commentsService.getCommentsByPost(pageable, postId);

        return BaseResponse.success(comments, ResultCode.OK);
    }

    @PutMapping("/{commentId}")
    public BaseResponse<CommentResponseDto> updateComment(@PathVariable Long postId,
                                                          @PathVariable Long commentId,
                                                          @RequestBody @Valid CommentUpdateRequestDto updateRequestDto,
                                                          @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto) {

        CommentResponseDto updatedComment = commentsService.updateComment(postId, commentId, updateRequestDto, userBaseDto);

        return BaseResponse.success(updatedComment, ResultCode.OK);
    }

    @DeleteMapping("/{commentId}")
    public BaseResponse<Void> deleteComment(@PathVariable Long postId,
                                            @PathVariable Long commentId,
                                            @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto) {

        commentsService.deleteComment(postId, commentId, userBaseDto);

        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }

}

