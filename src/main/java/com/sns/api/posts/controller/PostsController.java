package com.sns.api.posts.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.dto.request.PostCreateRequestDto;
import com.sns.api.posts.domain.dto.request.PostSearchRequestDto;
import com.sns.api.posts.domain.dto.request.PostUpdateRequestDto;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import com.sns.api.posts.service.PostsService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @PostMapping
    public BaseResponse<PostResponseDto> createPost(@RequestBody @Valid PostCreateRequestDto createRequestDto) {

        PostResponseDto savedPost = postsService.createPost(createRequestDto);

        return BaseResponse.success(savedPost, ResultCode.CREATED);
    }

    @GetMapping("/{postId}")
    public BaseResponse<PostResponseDto> getPost(@PathVariable Long postId) {

        PostResponseDto post = postsService.getPostById(postId);

        return BaseResponse.success(post, ResultCode.OK);
    }

    @GetMapping
    public BaseResponse<Page<PostResponseDto>> getPosts(
            @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute @Valid PostSearchRequestDto searchRequestDto
    ) {

        Page<PostResponseDto> posts = postsService.getPosts(pageable, searchRequestDto);

        return BaseResponse.success(posts, ResultCode.OK);
    }

    @PutMapping("/{postId}")
    public BaseResponse<PostResponseDto> updatePost(
            @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto,
            @PathVariable Long postId,
            @RequestBody @Valid PostUpdateRequestDto updateRequestDto
    ) {

        PostResponseDto updatedPost = postsService.updatePost(userBaseDto, postId, updateRequestDto);

        return BaseResponse.success(updatedPost, ResultCode.OK);
    }

    @DeleteMapping("/{postId}")
    public BaseResponse<Void> deletePost(
            @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto,
            @PathVariable Long postId
    ) {

        postsService.deletePost(userBaseDto, postId);

        return BaseResponse.success(null, ResultCode.OK);
    }
}
