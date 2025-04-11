package com.sns.api.posts.repository.query;

import com.sns.api.posts.domain.dto.request.PostSearchCondition;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostQueryRepository {

    Optional<PostResponseDto> findPostWithQuery(Long postId, Long userId);

    Page<PostResponseDto> findAllWithQuery(Long userId,
                                           PostSearchCondition searchCondition,
                                           Pageable pageable);

}
