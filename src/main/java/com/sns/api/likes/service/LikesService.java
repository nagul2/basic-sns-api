package com.sns.api.likes.service;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.likes.domain.dto.LikeResponseDto;
import com.sns.api.likes.domain.entity.LikeType;

public interface LikesService {

    LikeResponseDto createLike(Long likeTypeId, LikeType likeType, UserBaseDto userBaseDto);

    void deleteLike(Long likeTypeId, LikeType likeType, UserBaseDto userBaseDto);
}
