package com.sns.api.friends.service.impl;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.controller.SendFriendsRequestDto;
import com.sns.api.friends.domain.dto.FriendsResponseDto;
import com.sns.api.friends.domain.entity.Friends;
import com.sns.api.friends.repository.FriendsRepository;
import com.sns.api.friends.service.FriendsService;
import com.sns.api.users.domain.entity.Users;
import com.sns.api.users.repository.UsersRepository;
import com.sns.common.component.ResultCode;
import com.sns.common.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class FriendsServiceImpl implements FriendsService {

    private final FriendsRepository friendsRepository;
    private final UsersRepository usersRepository;

    /**
     * 친구 요청 저장
     *
     * @param requestDto 요청 받은 UserId
     * @param userBaseDto 요청 한 User Id
     * @return 응답 DTO
     */
    @Override
    @Transactional
    public FriendsResponseDto requestFriend(SendFriendsRequestDto requestDto, UserBaseDto userBaseDto) {

        Users findReceiveUser = findUserByIdOrElseThrow(requestDto.getReceiverId());
        Users findSendUser = findUserByIdOrElseThrow(userBaseDto.getUserId());

        Friends saveFriend = friendsRepository.save(Friends.of(findSendUser, findReceiveUser));

        return FriendsResponseDto.toMapDto(saveFriend);
    }

    /**
     * SendUser, ReceiveUser 찾고 못찾으면 예외 던지는 메서드
     *
     * @param userId 조회할 UserId
     * @return 성공하면 User, 실패하면 NOT_FOUND 예외
     */
    private Users findUserByIdOrElseThrow(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND));
    }
}
