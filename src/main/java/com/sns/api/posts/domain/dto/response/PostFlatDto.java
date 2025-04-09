package com.sns.api.posts.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostFlatDto {

    private Long postId;                // 게시물 ID
    
    private Long userId;                // 작성자 ID
    private String username;            // 작성자 이름

    private String content;             // 본문
    
    private Long commentCount;         // 댓글 개수

    private LocalDateTime createdAt;    // 생성일
    private LocalDateTime modifiedAt;   // 수정일

}
