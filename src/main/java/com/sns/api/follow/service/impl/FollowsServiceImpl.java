package com.sns.api.follow.service.impl;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.follow.domain.dto.request.FollowsRequestDto;
import com.sns.api.follow.domain.dto.response.FollowsResponseDto;
import com.sns.api.follow.domain.entity.Follows;
import com.sns.api.follow.repository.FollowsRepository;
import com.sns.api.follow.service.FollowsService;
import com.sns.api.users.domain.entity.Users;
import com.sns.api.users.repository.UsersRepository;
import com.sns.common.component.ResultCode;
import com.sns.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowsServiceImpl implements FollowsService {

    private final FollowsRepository followsRepository;

    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public FollowsResponseDto follow(FollowsRequestDto requestDto, UserBaseDto userBaseDto) {
        Long targetId = requestDto.getTargetId();
        Long followerId = userBaseDto.getUserId();

        if (targetId.equals(followerId)) {
            throw new CustomException(ResultCode.VALID_FAIL, "자기 자신을 팔로우 할 수 없습니다.");
        }

        Optional<Follows> followsOpt = followsRepository.findByFollowerIdAndFollowingId(followerId, targetId);

        if (followsOpt.isPresent()) {
            throw new CustomException(ResultCode.VALID_FAIL, "이미 팔로우 한 유저입니다.");
        }

        Users followingUser = usersRepository.findById(targetId)
                .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND, "해당 targetId 유저를 찾을 수 없습니다."));

        Follows follows = new Follows(followingUser);

        Follows saveFollows = followsRepository.save(follows);

        return FollowsResponseDto.fromEntity(saveFollows);
    }
}
