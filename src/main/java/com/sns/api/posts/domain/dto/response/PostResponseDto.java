package com.sns.api.posts.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.entity.Posts;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponseDto {

    private Long postId;                // 게시물 ID
    private UserBaseDto createdBy;      // 작성자 정보

    private String content;             // 본문

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long commentCount;          // 댓글 개수

    private LocalDateTime createdAt;    // 생성일
    private LocalDateTime modifiedAt;   // 수정일

    public static PostResponseDto fromEntity(Posts entity) {
        return new PostResponseDto(
                entity.getId(),
                UserBaseDto.fromEntity(entity.getCreatedBy()),
                entity.getContent(),
                null,
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    public static PostResponseDto fromFlatDto(PostFlatDto dto) {
        return new PostResponseDto(
                dto.getPostId(),
                UserBaseDto.builder()
                        .userId(dto.getUserId())
                        .username(dto.getUsername())
                        .build(),
                dto.getContent(),
                dto.getCommentCount(),
                dto.getCreatedAt(),
                dto.getModifiedAt()
        );
    }

}
