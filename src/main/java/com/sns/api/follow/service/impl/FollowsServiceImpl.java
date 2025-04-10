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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public void unFollow(Long followId, UserBaseDto userBaseDto) {
        Long userId = userBaseDto.getUserId();

        Follows follows = followsRepository.findById(followId)
                .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND, "해당 followId의 팔로우를 찾을 수 없습니다."));

        // 팔로워랑 팔로잉한 사람이 아닐 경우 권한 없음
        if (!follows.getFollower().getId().equals(userId) && !follows.getFollowing().getId().equals(userId)) {
            throw new CustomException(ResultCode.ACCESS_DENIED, "팔로우 취소 권한이 없습니다.");
        }

        followsRepository.delete(follows);
    }

    @Override
    public Page<FollowsResponseDto> getFollowers(UserBaseDto userBaseDto, Pageable pageable) {
        return followsRepository.findAllByFollowingIdWithActiveMember(userBaseDto.getUserId(), pageable)
                .map(FollowsResponseDto::fromEntity);
    }

    @Override
    public List<FollowsResponseDto> getFollowings(UserBaseDto userBaseDto) {
        return followsRepository.findAllByFollowerIdWithActiveMember(userBaseDto.getUserId())
                .stream()
                .map(FollowsResponseDto::fromEntity)
                .toList();
    }
}
