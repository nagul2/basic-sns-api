package com.sns.api.posts.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.dto.request.PostCreateRequestDto;
import com.sns.api.posts.domain.dto.request.PostSearchRequestDto;
import com.sns.api.posts.domain.dto.request.PostUpdateRequestDto;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import com.sns.api.posts.domain.dto.response.PostWithCommentsResponseDto;
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
    public BaseResponse<PostWithCommentsResponseDto> getPost(@PathVariable Long postId) {

        PostWithCommentsResponseDto postWithComments = postsService.getPostById(postId);

        return BaseResponse.success(postWithComments, ResultCode.OK);
    }

    @GetMapping
    public BaseResponse<Page<PostResponseDto>> getPosts(@ModelAttribute @Valid PostSearchRequestDto searchRequestDto,
                                                        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                        @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto) {

        Page<PostResponseDto> posts = postsService.getPosts(searchRequestDto, pageable, userBaseDto);

        return BaseResponse.success(posts, ResultCode.OK);
    }

    @PutMapping("/{postId}")
    public BaseResponse<PostResponseDto> updatePost(@PathVariable Long postId,
                                                    @RequestBody @Valid PostUpdateRequestDto updateRequestDto,
                                                    @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto) {

        PostResponseDto updatedPost = postsService.updatePost(postId, updateRequestDto, userBaseDto);

        return BaseResponse.success(updatedPost, ResultCode.OK);
    }

    @DeleteMapping("/{postId}")
    public BaseResponse<Void> deletePost(@PathVariable Long postId,
                                         @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto) {

        postsService.deletePost(postId, userBaseDto);

        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }
}
