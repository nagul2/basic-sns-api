package com.sns.api.posts.domain.dto.response;

import com.sns.api.comments.domain.dto.response.CommentResponseDto;
import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.entity.Posts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostWithCommentsResponseDto {

    private Long postId;                        // 게시물 ID
    private UserBaseDto createdBy;              // 작성자 정보

    private String content;                     // 본문

    private LocalDateTime createdAt;            // 생성일
    private LocalDateTime modifiedAt;           // 수정일
    
    private Page<CommentResponseDto> commentList;     // 댓글 리스트

    public static PostWithCommentsResponseDto fromEntity(Posts entity, Page<Comments> comments) {
        return new PostWithCommentsResponseDto(
                entity.getId(),
                UserBaseDto.fromEntity(entity.getCreatedBy()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                comments.map(CommentResponseDto::fromEntity)
        );
    }

}
