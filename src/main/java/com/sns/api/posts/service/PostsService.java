package com.sns.api.posts.service;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.dto.request.PostCreateRequestDto;
import com.sns.api.posts.domain.dto.request.PostSearchRequestDto;
import com.sns.api.posts.domain.dto.request.PostUpdateRequestDto;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsService {

    PostResponseDto createPost(PostCreateRequestDto createRequestDto);

    PostResponseDto getPostById(Long postId);

    Page<PostResponseDto> getPosts(Pageable pageable, PostSearchRequestDto searchRequestDto);

    PostResponseDto updatePost(UserBaseDto userBaseDto, Long postId, PostUpdateRequestDto updateRequestDto);

    void deletePost(UserBaseDto userBaseDto, Long postId);

}
