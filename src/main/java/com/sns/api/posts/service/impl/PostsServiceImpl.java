package com.sns.api.posts.service.impl;

import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.comments.repository.CommentsRepository;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.posts.domain.dto.request.PostCreateRequestDto;
import com.sns.api.posts.domain.dto.request.PostSearchRequestDto;
import com.sns.api.posts.domain.dto.request.PostUpdateRequestDto;
import com.sns.api.posts.domain.dto.response.PostFlatDto;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import com.sns.api.posts.domain.dto.response.PostWithCommentsResponseDto;
import com.sns.api.posts.domain.entity.Posts;
import com.sns.api.posts.repository.PostsRepository;
import com.sns.api.posts.service.PostsService;
import com.sns.api.users.domain.entity.Users;
import com.sns.api.users.repository.UsersRepository;
import com.sns.common.component.ResultCode;
import com.sns.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;

    private final UsersRepository usersRepository;
    private final CommentsRepository commentsRepository;

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

    /**
     * 게시물 단건 조회
     * 게시물에 속한 댓글들도 함께 조회한다.
     * 댓글은 기본적으로 0 페이지의 데이터를 조회하며, 만약 추가적인 데이터가 필요하다면 GET `/api/posts/{postId}/comments` API 사용
     * @see com.sns.api.comments.controller.CommentsController#getComments(Long, Pageable)
     *
     * @param postId 조회할 게시물 ID
     * @return 댓글 리스트를 포함한 게시물 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    @Override
    public PostWithCommentsResponseDto getPostById(Long postId) {

        Posts post = getPostByIdOrElseThrow(postId);

        // 댓글 조회
        Page<Comments> comments = commentsRepository.findAllByPost(
                post,
                PageRequest.of(0, 10, Sort.by("createdBy").descending())
        );

        return PostWithCommentsResponseDto.fromEntity(post, comments);
    }

    /**
     * 기본 정렬은 생성일자 기준으로 내림차순 정렬
     * 10개씩 페이지네이션
     * @param pageable 페이징 정보
     * @return Page 객체
     */
    @Transactional(readOnly = true)
    @Override
    public Page<PostResponseDto> getPosts(PostSearchRequestDto searchRequestDto, Pageable pageable) {

        // 기간별 검색
        // startDate, endDate 모두 null 값이 아니고, startDate <= endDate 이어야 기간별 검색 쿼리를 수행
        if (searchRequestDto.hasValidValue()) {
            Page<PostFlatDto> findPosts = postsRepository.findPostsByCreatedAt(
                    searchRequestDto.getStartDate(),
                    searchRequestDto.getEndDate(),
                    pageable
            );
            return findPosts.map(PostResponseDto::fromFlatDto);
        }
        
        // 일반 검색
        return postsRepository.findAllWithCommentCount(pageable).map(PostResponseDto::fromFlatDto);
    }

    @Transactional
    @Override
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto updateRequestDto, UserBaseDto userBaseDto) {

        Users user = getUserByIdOrElseThrow(userBaseDto.getUserId());
        Posts post = getPostByIdOrElseThrow(postId);

        verifyOwnPost(user.getId(), post);

        post.updateContent(updateRequestDto.getContent());

        return PostResponseDto.fromEntity(post);
    }

    @Transactional
    @Override
    public void deletePost(Long postId, UserBaseDto userBaseDto) {

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
