package com.sns.api.comments.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.common.domain.dto.UserBaseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long commentId;             // 댓글 ID
    private final Long postId;                // 게시물 ID
    private final UserBaseDto createdBy;      // 작성자 정보
    private final UserBaseDto modifiedBy;     // 수정자 정보
    
    private final String content;             // 본문

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long likeCount;             // 좋아요 개수

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Boolean isLiked;            // 좋아요 여부

    private final LocalDateTime createdAt;    // 생성일
    private final LocalDateTime modifiedAt;   // 수정일

    public static CommentResponseDto fromEntity(Comments entity) {
        return new CommentResponseDto(
                entity.getId(),
                entity.getPost().getId(),
                UserBaseDto.fromEntity(entity.getCreatedBy()),
                UserBaseDto.fromEntity(entity.getModifiedBy()),
                entity.getContent(),
                null,
                null,
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    @QueryProjection
    public CommentResponseDto(Long commentId, Long postId, UserBaseDto createdBy, UserBaseDto modifiedBy, String content, Long likeCount, Boolean isLiked, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.content = content;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}
