package com.sns.api.posts.service.impl;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.dto.request.PostCreateRequestDto;
import com.sns.api.posts.domain.dto.request.PostSearchRequestDto;
import com.sns.api.posts.domain.dto.request.PostUpdateRequestDto;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import com.sns.api.posts.domain.entity.Posts;
import com.sns.api.posts.repository.PostsRepository;
import com.sns.api.posts.service.PostsService;
import com.sns.api.users.domain.entity.Users;
import com.sns.api.users.repository.UsersRepository;
import com.sns.common.component.ResultCode;
import com.sns.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    @Override
    public PostResponseDto createPost(PostCreateRequestDto createRequestDto) {

        Posts savedPost = postsRepository.save(
                Posts.of(
                        createRequestDto.getContent()
                )
        );

        return PostResponseDto.fromEntity(savedPost);
    }

    @Transactional(readOnly = true)
    @Override
    public PostResponseDto getPostById(Long postId) {

        Posts post = getPostByIdOrElseThrow(postId);

        return PostResponseDto.fromEntity(post);
    }

    /**
     * 기본 정렬은 생성일자 기준으로 내림차순 정렬
     * 10개씩 페이지네이션
     * @param pageable 페이징 정보
     * @return Page 객체
     */
    @Transactional(readOnly = true)
    @Override
    public Page<PostResponseDto> getPosts(Pageable pageable, PostSearchRequestDto searchRequestDto) {

        // 기간별 검색
        // startDate, endDate 모두 null 값이 아니고, startDate <= endDate 이어야 기간별 검색 쿼리를 수행
        if (searchRequestDto.hasValidValue()) {
            Page<Posts> findPosts = postsRepository.findPostsByCreatedAt(
                    pageable,
                    searchRequestDto.getStartDate(),
                    searchRequestDto.getEndDate()
            );
            return findPosts.map(PostResponseDto::fromEntity);
        }
        
        // 일반 검색
        return postsRepository.findAll(pageable).map(PostResponseDto::fromEntity);
    }

    @Transactional
    @Override
    public PostResponseDto updatePost(UserBaseDto userBaseDto, Long postId, PostUpdateRequestDto updateRequestDto) {

        Users user = getUserByIdOrElseThrow(userBaseDto.getUserId());
        Posts post = getPostByIdOrElseThrow(postId);

        verifyOwnPost(user.getId(), post);

        post.updateContent(updateRequestDto.getContent());

        return PostResponseDto.fromEntity(post);
    }

    @Transactional
    @Override
    public void deletePost(UserBaseDto userBaseDto, Long postId) {

        Users user = getUserByIdOrElseThrow(userBaseDto.getUserId());
        Posts post = getPostByIdOrElseThrow(postId);

        verifyOwnPost(user.getId(), post);

        postsRepository.delete(post);
    }


    private Users getUserByIdOrElseThrow(Long userId) {

        return usersRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND, "존재하지 않는 회원 ID 입니다: " + userId));
    }

    private Posts getPostByIdOrElseThrow(Long postId) {

        return postsRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND, "존재하지 않는 게시글 ID 입니다.: " + postId));
    }

    /**
     * 수정, 삭제하려는 회원이 게시물을 작성한 회원과 동일한지 검증하는 메서드
     * @param userId 수정, 삭제를 요청한 회원의 ID
     * @param post 대상 게시물
     */
    private void verifyOwnPost(Long userId, Posts post) {

        if (!Objects.equals(userId, post.getCreatedBy().getId())) {
            throw new CustomException(ResultCode.ACCESS_DENIED, "게시물의 수정, 삭제는 해당 게시물을 작성한 회원만 가능합니다.");
        }
    }

}
