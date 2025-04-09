package com.sns.api.comments.domain.dto.response;

import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.common.domain.dto.UserBaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {

    private Long commentId;             // 댓글 ID
    private Long postId;                // 게시물 ID
    private UserBaseDto createdBy;      // 작성자 정보
    private UserBaseDto modifiedBy;     // 수정자 정보
    
    private String content;             // 본문

    private LocalDateTime createdAt;    // 생성일
    private LocalDateTime modifiedAt;   // 수정일

    public static CommentResponseDto fromEntity(Comments entity) {
        return new CommentResponseDto(
                entity.getId(),
                entity.getPost().getId(),
                UserBaseDto.fromEntity(entity.getCreatedBy()),
                UserBaseDto.fromEntity(entity.getModifiedBy()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

}
