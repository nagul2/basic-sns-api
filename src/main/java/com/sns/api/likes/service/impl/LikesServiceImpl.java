package com.sns.api.likes.service.impl;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.likes.domain.dto.LikeResponseDto;
import com.sns.api.likes.domain.entity.LikeType;
import com.sns.api.likes.domain.entity.Likes;
import com.sns.api.likes.repository.LikesRepository;
import com.sns.api.likes.service.LikesService;
import com.sns.api.posts.domain.entity.Posts;
import com.sns.api.posts.repository.PostsRepository;
import com.sns.common.component.ResultCode;
import com.sns.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;

    private final PostsRepository postsRepository;

    @Override
    public LikeResponseDto createLike(Long likeTypeId, LikeType likeType, UserBaseDto userBaseDto) {

        // 해당 게시글이 있는지 조회
        Posts posts = findByIdOrElseThrow(likeTypeId);

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

    private Posts findByIdOrElseThrow(Long id) {

        return postsRepository.findById(id).orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND));
    }
}
