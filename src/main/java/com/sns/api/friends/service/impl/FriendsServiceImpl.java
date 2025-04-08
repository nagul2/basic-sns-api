package com.sns.api.friends.service.impl;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.friends.domain.dto.request.ActionFriendsRequestDto;
import com.sns.api.friends.domain.dto.request.SendFriendsRequestDto;
import com.sns.api.friends.domain.dto.response.CommonFriendsResponseDto;
import com.sns.api.friends.domain.entity.Friends;
import com.sns.api.friends.domain.entity.FriendsStatus;
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
    public CommonFriendsResponseDto requestFriend(SendFriendsRequestDto requestDto, UserBaseDto userBaseDto) {

        Users findReceiveUser = findUserByIdOrElseThrow(requestDto.getReceiverId());
        Users findSendUser = findUserByIdOrElseThrow(userBaseDto.getUserId());

        if (findReceiveUser.equals(findSendUser)) {
            throw new CustomException(ResultCode.VALID_FAIL, "동일한 대상에게는 친구 요청을 할 수 없습니다.");
        }

        if (friendsRepository.existsByFromUserAndToUser(findSendUser, findReceiveUser)) {
            throw new CustomException(ResultCode.VALID_FAIL, "이미 친구 요청을 보냈습니다.");
        }

        Friends saveFriend = friendsRepository.save(Friends.of(findSendUser, findReceiveUser));

        return CommonFriendsResponseDto.toMapDto(saveFriend);
    }

    /**
     * 친구 수락/거절/취소 처리
     *
     * @param friendsId 요청한 Friends 테이블의 PK 값
     * @param requestDto 요청 상태값
     * @param userBaseDto 로그인 유저 정보
     * @return 수락 시 응답 DTO, 거절 및 취소 시 null 반환
     */
    @Override
    @Transactional
    public CommonFriendsResponseDto actionFriend(Long friendsId, ActionFriendsRequestDto requestDto, UserBaseDto userBaseDto) {

        Friends findFriends = findFriendsByIdOrElseThrow(friendsId);
        Long loginUserId = userBaseDto.getUserId();
        Long senderId = findFriends.getFromUser().getId();
        Long receiverId = findFriends.getToUser().getId();

        if (!loginUserId.equals(senderId) && !loginUserId.equals(receiverId)) {
            throw new CustomException(ResultCode.ACCESS_DENIED, "접근 권한이 없습니다.");
        }

        if (findFriends.getStatus() == FriendsStatus.ACCEPT) {
            throw new CustomException(ResultCode.VALID_FAIL, "이미 수락된 친구입니다.");
        }

        return actionFriendStatus(requestDto, findFriends);
    }

    /**
     * SendUser, ReceiveUser 찾고 못찾으면 예외 던지는 메서드
     *
     * @param userId 조회할 UserId
     * @return 성공하면 Users, 실패하면 NOT_FOUND 예외
     */
    private Users findUserByIdOrElseThrow(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    /**
     * friend 찾고 못찾으면 예외 던지는 메서드
     *
     * @param friendsId 조회할 FriendsId
     * @return 성공하면 Friends, 실패하면 NOT_FOUND
     */
    private Friends findFriendsByIdOrElseThrow(Long friendsId) {
        return friendsRepository.findById(friendsId)
                .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND, "친구 요청 정보를 찾을 수 없습니다."));
    }

    /**
     * 수락, 거절, 취소 상태에 따라 친구 요청 상태를 처리하는 메서드
     * 요청 수락 -> ACCEPT 상태 반영 및 DTO 변환 후 반환
     * 요청 거절, 취소 -> Friends 테이블에서 데이터 삭제 및 null 반환
     * PENDING 및 잘못된 요청 시 400 예외 발생
     *
     * @param requestDto
     * @param findFriends
     * @return
     */
    private CommonFriendsResponseDto actionFriendStatus(ActionFriendsRequestDto requestDto, Friends findFriends) {
        switch (requestDto.getStatus()) {
            case ACCEPT -> {
                findFriends.accept();
                return CommonFriendsResponseDto.toMapDto(findFriends);
            }
            case CANCEL, REFUSAL -> {
                friendsRepository.delete(findFriends);
                return null;
            }

            default -> throw new CustomException(ResultCode.VALID_FAIL, "잘못된 요청값 입니다.");
        }
    }
}
