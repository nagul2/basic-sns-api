package com.sns.api.posts.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.entity.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private final Long postId;                  // 게시물 ID
    private final UserBaseDto createdBy;        // 작성자 정보

    private final String content;               // 본문

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long commentCount;            // 댓글 개수

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long likeCount;               // 좋아요 개수

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Boolean isLiked;              // 좋아요 여부

    private final LocalDateTime createdAt;      // 생성일
    private final LocalDateTime modifiedAt;     // 수정일

    public static PostResponseDto fromEntity(Posts entity) {
        return new PostResponseDto(
                entity.getId(),
                UserBaseDto.fromEntity(entity.getCreatedBy()),
                entity.getContent(),
                null,
                null,
                null,
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    @QueryProjection
    public PostResponseDto(Long postId,
                           UserBaseDto createdBy,
                           String content,
                           Long commentCount,
                           Long likeCount,
                           Boolean isLiked,
                           LocalDateTime createdAt,
                           LocalDateTime modifiedAt) {
        this.postId = postId;
        this.createdBy = createdBy;
        this.content = content;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}
