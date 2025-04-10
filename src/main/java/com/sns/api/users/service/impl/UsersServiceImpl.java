package com.sns.api.users.service.impl;

import com.sns.api.friends.repository.FriendsRepository;
import com.sns.api.users.domain.dto.UserReadResponseDto;
import com.sns.api.users.domain.dto.UserUpdateRequestDto;
import com.sns.api.users.domain.dto.PasswordUpdateDto;
import com.sns.api.users.domain.dto.UserDeleteRequestDto;
import com.sns.api.users.domain.dto.UsersResponseDto;
import com.sns.api.users.domain.entity.Users;

import com.sns.api.users.repository.UsersRepository;
import com.sns.api.users.service.UsersService;
import com.sns.common.component.ResultCode;
import com.sns.common.config.PasswordEncoder;
import com.sns.common.exception.CustomException;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final FriendsRepository friendsRepository;

    /**
     * 내 정보 조회
     *
     * @param id 조회할 본인 id
     * @return 내 정보를 담은 dto
     */
    @Override
    public UsersResponseDto getMyInfo(Long id) {

        Users user = findByIdOrElseThrow(id);

        return UsersResponseDto.fromEntity(user);
    }

    /**
     * 내 정보 수정
     *
     * @param id 수정할 본인 id
     * @param dto 수정할 정보
     * @return 수정한 내 정보를 담은 dto
     */
    @Override
    @Transactional
    public UsersResponseDto updateMyInfo(Long id, UserUpdateRequestDto dto) {

        Users user = findByIdOrElseThrow(id);

        user.updateMyInfo(dto.getUsername(), dto.getBirth(), dto.getMbti());
        usersRepository.save(user);

        return UsersResponseDto.fromEntity(user);
    }

    public void deleteMe(Long id, UserDeleteRequestDto requestDto) {
        Users user = findByIdOrElseThrow(id);

        String password = requestDto.getPassword();

        if (!passwordEncoder.matches(password, user.getPassword())) { // 비밀번호 검증
            throw new CustomException(ResultCode.VALID_FAIL, "비밀번호가 일치하지 않습니다."); // 비밀번호 검증 실패 시 throw
        }

        usersRepository.delete(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long id, PasswordUpdateDto updateDto) {
        Users user = findByIdOrElseThrow(id);

        String newPassword = updateDto.getNewPassword();
        String currentPassword = updateDto.getCurrentPassword();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) { // 비밀번호 검증
            throw new CustomException(ResultCode.VALID_FAIL, "비밀번호가 일치하지 않습니다."); // 비밀번호 검증 실패 시 throw
        }

        if (StringUtils.equals(newPassword, currentPassword)) {
            throw new CustomException(ResultCode.VALID_FAIL, "현재 사용 중인 비밀번호입니다."); // 비밀번호 검증 실패 시 throw
        }

        String encodePassword = passwordEncoder.encode(newPassword); // 비밀번호 암호화
        user.updatePassword(encodePassword);
        usersRepository.save(user);
    }

    /**
     * 회원 정보 조회
     *
     * @param id 조회할 회원 id
     * @return 조회된 회원 정보를 담은 dto
     */
    @Override
    public UserReadResponseDto findById(Long id) {

        Users user = findByIdOrElseThrow(id);

        return UserReadResponseDto.fromEntity(user);
    }

    /**
     * 회원 검색
     *
     * @param pageable 페이징 정보
     * @param username 검색할 회원 이름
     * @param email 검색할 회원 이메일
     * @param userId 로그인한 유저 id
     * @return 검색된 회원 정보를 담은 dto page
     */
    @Override
    public Page<UserReadResponseDto> searchUsers(Pageable pageable, String username, String email, Long userId) {

        // 로그인한 유저의 친구 id 목록을 조회
        Set<Long> friendsIds = friendsRepository.findAcceptedFriendsByLoginUserId(userId, Pageable.unpaged()).stream()
                .map(f -> f.getFromUser().getId().equals(userId) ? f.getToUser().getId() : f.getFromUser().getId())
                .collect(Collectors.toSet());

        return usersRepository.searchByUsernameAndEmail(pageable, username, email, friendsIds)
                .map(UserReadResponseDto::fromEntity);
    }

    /**
     * 회원을 조회하고 없으면 예외를 발생시키는 메서드
     *
     * @param id 조회할 회원 id
     * @return 회원이 존재하면 users, 존재하지 않으면 NOT_FOUND
     */
    private Users findByIdOrElseThrow(Long id) {

        return usersRepository.findById(id).orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND, "해당 회원이 존재하지 않습니다.: " + id));
    }
}