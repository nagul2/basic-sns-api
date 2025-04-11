package com.sns.api.follow.service.impl;

import com.sns.api.common.domain.dto.PageResponseDto;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowsServiceImpl implements FollowsService {

    private final FollowsRepository followsRepository;

    private final UsersRepository usersRepository;

    /**
     * 팔로우 저장
     *
     * @param requestDto  팔로잉 user
     * @param userBaseDto 팔로워 user
     * @return 응답 DTO
     */
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

    /**
     * 팔로우 취소
     *
     * @param followId    요청한 follows 테이블의 PK 값
     * @param userBaseDto 로그인 유저 정보
     *                    팔로잉, 팔로워 둘다 속하지 않으면 exception
     */
    @Override
    @Transactional
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

    /**
     * 로그인한 사용자의 팔로워 조회 로직
     *
     * @param userBaseDto 로그인 유저 정보
     * @param pageable    페이징 정보
     * @return 조회된 PageResponseDto<DTO>
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<FollowsResponseDto> getFollowers(UserBaseDto userBaseDto, Pageable pageable) {
        Page<FollowsResponseDto> result = followsRepository.findAllByFollowingIdWithActiveMember(userBaseDto.getUserId(), pageable)
                .map(FollowsResponseDto::fromEntity);

        return PageResponseDto.toDto(result);
    }

    /**
     * 로그인한 사용자의 팔로잉 조회 로직
     *
     * @param userBaseDto 로그인 유저 정보
     * @param pageable    페이징 정보
     * @return 조회된 PageResponseDto<DTO>
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<FollowsResponseDto> getFollowings(UserBaseDto userBaseDto, Pageable pageable) {
        Page<FollowsResponseDto> result = followsRepository.findAllByFollowerIdWithActiveMember(userBaseDto.getUserId(), pageable)
                .map(FollowsResponseDto::fromEntity);

        return PageResponseDto.toDto(result);

    }
}
