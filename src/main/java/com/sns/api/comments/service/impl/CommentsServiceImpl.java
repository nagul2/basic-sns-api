package com.sns.api.comments.service.impl;

import com.sns.api.comments.domain.dto.request.CommentCreateRequestDto;
import com.sns.api.comments.domain.dto.request.CommentUpdateRequestDto;
import com.sns.api.comments.domain.dto.response.CommentResponseDto;
import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.comments.repository.CommentsRepository;
import com.sns.api.comments.service.CommentsService;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.entity.Posts;
import com.sns.api.posts.repository.PostsRepository;
import com.sns.common.component.ResultCode;
import com.sns.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;

    @Transactional
    @Override
    public CommentResponseDto createComment(Long postId, CommentCreateRequestDto createRequestDto) {

        Posts post = getPostByIdOrElseThrow(postId);

        Comments savedComment = commentsRepository.save(
                Comments.of(
                        post,
                        createRequestDto.getContent()
                )
        );

        return CommentResponseDto.fromEntity(savedComment);
    }

    @Transactional(readOnly = true)
    @Override
    public CommentResponseDto getCommentById(Long postId, Long commentId) {

        Comments comment = getCommentByIdOrElseThrow(commentId);

        // 유효성 검사
        validateCommentBelongsToPost(postId, comment);

        return CommentResponseDto.fromEntity(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CommentResponseDto> getCommentsByPost(Pageable pageable, Long postId) {

        Posts post = getPostByIdOrElseThrow(postId);

        Page<Comments> comments = commentsRepository.findAllByPost(post, pageable);

        return comments.map(CommentResponseDto::fromEntity);
    }

    /**
     * 댓글의 수정은 댓글의 작성자 혹은 게시글의 작성자만 가능
     * @param userBaseDto 댓글 수정 요청 회원
     * @param commentId 댓글 ID
     * @param updateRequestDto 댓글 수정 내용
     * @return 수정된 댓글 DTO
     */
    @Transactional
    @Override
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentUpdateRequestDto updateRequestDto, UserBaseDto userBaseDto) {

        Comments comment = getCommentByIdOrElseThrow(commentId);
        
        // 유효성 검사
        validateCommentBelongsToPost(postId, comment);
        validateUpdateOrDeleteAuthority(
                userBaseDto.getUserId(),
                comment.getPost().getCreatedBy().getId(),
                comment.getCreatedBy().getId()
        );

        comment.updateComment(updateRequestDto.getContent());
        
        return CommentResponseDto.fromEntity(comment);
    }

    /**
     * 댓글의 삭제는 댓글의 작성자 혹은 게시글의 작성자만 가능
     * @param userBaseDto 댓글 삭제 요청 회원
     * @param commentId 댓글 ID
     */
    @Transactional
    @Override
    public void deleteComment(Long postId, Long commentId, UserBaseDto userBaseDto) {

        Comments comment = getCommentByIdOrElseThrow(commentId);

        // 유효성 검사
        validateCommentBelongsToPost(postId, comment);
        validateUpdateOrDeleteAuthority(
                userBaseDto.getUserId(),
                comment.getPost().getCreatedBy().getId(),
                comment.getCreatedBy().getId()
        );

        commentsRepository.delete(comment);
    }


    private Comments getCommentByIdOrElseThrow(Long commentId) {

        return commentsRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND, "존재하지 않는 댓글 ID입니다: " + commentId));
    }

    private Posts getPostByIdOrElseThrow(Long postId) {

        return postsRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND, "존재하지 않는 게시물 ID입니다: " + postId));
    }

    /**
     * 댓글 수정, 삭제 시 댓글이 게시물에 속하는지 확인하는 메서드
     * @param postId path variable 로 전달된 게시물 ID
     * @param comment 댓글 entity
     */
    public void validateCommentBelongsToPost(Long postId, Comments comment) {

        if (!Objects.equals(postId, comment.getPost().getId())) {
            throw new CustomException(ResultCode.VALID_FAIL, "댓글은 해당 게시물에 속하지 않습니다.");
        }
    }

    /**
     * 댓글의 수정, 삭제를 요청한 회원에게 권한이 있는지 검사하고, 없다면 예외를 발생시키는 메서드
     * 댓글 수정, 삭제는 댓글의 작성자 혹은 게시글의 작성자만 가능하다.
     * @param userId 요청 회원 ID
     * @param postWriterId 게시물 작성자 ID
     * @param commentWriterId 댓글 작성자 ID
     */
    private void validateUpdateOrDeleteAuthority(Long userId, Long postWriterId, Long commentWriterId) {

        boolean authorized = Objects.equals(userId, postWriterId) || Objects.equals(userId, commentWriterId);

        // 수정, 삭제하려는 댓글의 작성자이거나, 게시글의 작성자이면 통과
        if (!authorized) {
            throw new CustomException(ResultCode.ACCESS_DENIED, "댓글의 수정, 삭제는 글의 작성자 혹은 게시글의 작성자만 가능합니다.");
        }

    }

}
