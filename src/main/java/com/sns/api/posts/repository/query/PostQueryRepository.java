package com.sns.api.posts.repository.query;

import com.sns.api.posts.domain.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PostQueryRepository {

    Page<PostResponseDto> findAllWithQuery(Long userId,
                                           LocalDateTime startDate,
                                           LocalDateTime endDate,
                                           Pageable pageable);

}
