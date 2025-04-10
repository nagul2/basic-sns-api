package com.sns.api.comments.repository.query;

import com.sns.api.comments.domain.dto.response.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentsQueryRepository {

    Page<CommentResponseDto> findAllWithQuery(Long postId, Long userId, Pageable pageable);

}
