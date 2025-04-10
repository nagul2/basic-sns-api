package com.sns.api.likes.service.impl;

import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.comments.repository.CommentsRepository;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.likes.domain.dto.LikeCountResponseDto;
import com.sns.api.likes.domain.dto.LikeResponseDto;
import com.sns.api.likes.domain.entity.LikeType;
import com.sns.api.likes.domain.entity.Likes;
import com.sns.api.likes.repository.LikesRepository;
import com.sns.api.likes.service.LikesService;
import com.sns.api.posts.domain.entity.Posts;
import com.sns.api.posts.repository.PostsRepository;
import com.sns.common.component.ResultCode;
import com.sns.common.exception.CustomException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;

    private final PostsRepository postsRepository;

    private final CommentsRepository commentsRepository;

    @Override
    public LikeResponseDto createLike(Long likeTypeId, LikeType likeType, UserBaseDto userBaseDto) {

        // 본인이 작성한 댓글/게시글인지 확인
        validateCreateLike(likeTypeId, likeType, userBaseDto.getUserId());

        // 이미 좋아요를 눌렀는지 조회
        boolean isExist = likesRepository.existsByLikeTypeAndLikeTypeIdAndCreatedById(likeType, likeTypeId,
                userBaseDto.getUserId());

        if (isExist) {
            throw new CustomException(ResultCode.VALID_FAIL, "이미 좋아요를 눌렀습니다.");
        }

        Likes like = new Likes(likeType, likeTypeId);
        Likes savedLike = likesRepository.save(like);

        return LikeResponseDto.fromEntity(savedLike);
    }

    @Override
    public void deleteLike(Long likeTypeId, LikeType likeType, UserBaseDto userBaseDto) {

        // 해당 게시글이 있는지 조회
        Posts posts = findByIdOrElseThrow(likeTypeId);

        // 자신이 누른 좋아요를 조회 (하나의 게시글에는 하나의 결과만 나옴)
        Likes likes = likesRepository.findByLikeTypeAndLikeTypeIdAndCreatedById(likeType, likeTypeId,
                        userBaseDto.getUserId())
                .orElseThrow(() -> new CustomException(ResultCode.VALID_FAIL, "삭제할 좋아요가 없습니다."));

        likesRepository.delete(likes);
    }

    @Override
    public LikeCountResponseDto countLike(Long likeTypeId, LikeType likeType) {

        // 해당 게시글이 있는지 조회
        Posts posts = findByIdOrElseThrow(likeTypeId);

        // 해당 게시글의 좋아요 수를 계산
        Long likeCount = likesRepository.countByLikeTypeAndLikeTypeId(likeType, likeTypeId);

        return LikeCountResponseDto.fromEntity(likeTypeId, likeType, likeCount);
    }

    private Posts findByIdOrElseThrow(Long id) {

        return postsRepository.findById(id).orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND));
    }

    private void validateCreateLike(Long id, LikeType likeType, Long userId) {

        switch (likeType) {
            case POST -> {
                Posts posts = postsRepository.findById(id).orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND));

                if (Objects.equals(posts.getCreatedBy().getId(), userId)) {
                    throw new CustomException(ResultCode.VALID_FAIL, "본인이 작성한 게시글에는 좋아요를 남길 수 없습니다.");
                }
            }
            case COMMENT -> {
                Comments comments = commentsRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND));

                if (Objects.equals(comments.getCreatedBy().getId(), userId)) {
                    throw new CustomException(ResultCode.VALID_FAIL, "본인이 작성한 댓글에는 좋아요를 남길 수 없습니다.");
                }
            }
        }
    }
}
