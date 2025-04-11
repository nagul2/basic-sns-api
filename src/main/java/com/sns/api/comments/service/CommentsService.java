package com.sns.api.comments.service;

import com.sns.api.comments.domain.dto.request.CommentCreateRequestDto;
import com.sns.api.comments.domain.dto.request.CommentUpdateRequestDto;
import com.sns.api.comments.domain.dto.response.CommentResponseDto;
import com.sns.api.common.domain.dto.UserBaseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentsService {

    CommentResponseDto createComment(Long postId, CommentCreateRequestDto createRequestDto);

    CommentResponseDto getCommentById(Long postId, Long commentId);

    Page<CommentResponseDto> getCommentsByPost(Long postId, UserBaseDto userBaseDto, Pageable pageable);

    CommentResponseDto updateComment(Long postId, Long commentId, CommentUpdateRequestDto updateRequestDto, UserBaseDto userBaseDto);

    void deleteComment(Long postId, Long commentId, UserBaseDto userBaseDto);

}
