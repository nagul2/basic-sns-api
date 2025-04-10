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

    /**
     * 게시물 단건 조회 API
     * 
     * @param postId        게시물 ID
     * @param userBaseDto   로그인한 회원 정보
     *                      
     * @return  성공 시 DTO 및 200 응답
     */
    @GetMapping("/{postId}")
    public BaseResponse<PostWithCommentsResponseDto> getPost(@PathVariable Long postId,
                                                             @SessionAttribute(name = Const.LOGIN_USER) UserBaseDto userBaseDto) {

        PostWithCommentsResponseDto postWithComments = postsService.getPostById(postId, userBaseDto);

        return BaseResponse.success(postWithComments, ResultCode.OK);
    }

    /**
     * 게시물 전체 조회 API
     *
     * @param searchRequestDto  검색 조건 파라미터 (게시물 작성일 범위 등)
     * @param pageable          생성일 기준 내림차순이 디폴트
     *                          댓글순(comment), 좋아요순(like), 수정일(modifiedAt) 등 다양한 정렬 조건 사용 가능
     * @param userBaseDto       로그인한 회원 정보
     *                          
     * @return 성공 시 DTO 및 200 응답
     */
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
