package com.sns.api.posts.service;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.dto.request.PostCreateRequestDto;
import com.sns.api.posts.domain.dto.request.PostSearchCondition;
import com.sns.api.posts.domain.dto.request.PostUpdateRequestDto;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import com.sns.api.posts.domain.dto.response.PostWithCommentsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsService {

    PostResponseDto createPost(PostCreateRequestDto createRequestDto);

    PostWithCommentsResponseDto getPostById(Long postId, UserBaseDto userBaseDto);

    Page<PostResponseDto> getPosts(PostSearchCondition searchRequestDto, Pageable pageable, UserBaseDto userBaseDto);

    PostResponseDto updatePost(Long postId, PostUpdateRequestDto updateRequestDto, UserBaseDto userBaseDto);

    void deletePost(Long postId, UserBaseDto userBaseDto);

}
