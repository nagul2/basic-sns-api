package com.sns.api.users.service.impl;

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

    @Override
    public UsersResponseDto getMyInfo(Long id) {

        Users user = findByIdOrElseThrow(id);

        return UsersResponseDto.fromEntity(user);
    }

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

    @Override
    public UserReadResponseDto findById(Long id) {

        Users user = findByIdOrElseThrow(id);

        return UserReadResponseDto.fromEntity(user);
    }

    @Override
    public Page<UserReadResponseDto> searchUsers(Pageable pageable, String username, String email) {

        return usersRepository.searchByUsernameAndEmail(pageable, username, email).map(UserReadResponseDto::fromEntity);
    }

    private Users findByIdOrElseThrow(Long id) {

        return usersRepository.findById(id).orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND));
    }
}