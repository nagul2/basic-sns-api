package com.sns.api.posts.domain.dto.response;

import com.sns.api.comments.domain.dto.response.CommentResponseDto;
import com.sns.api.comments.domain.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class PostWithCommentsResponseDto {

    private PostResponseDto post;
    
    private Page<CommentResponseDto> commentList;     // 댓글 리스트

    public static PostWithCommentsResponseDto of(PostResponseDto postResponseDto, Page<Comments> comments) {
        return new PostWithCommentsResponseDto(
                postResponseDto,
                comments.map(CommentResponseDto::fromEntity)
        );
    }

}
