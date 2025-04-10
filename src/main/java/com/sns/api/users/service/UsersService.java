package com.sns.api.users.service;

import com.sns.api.users.domain.dto.UserReadResponseDto;
import com.sns.api.users.domain.dto.UserUpdateRequestDto;
import com.sns.api.users.domain.dto.PasswordUpdateDto;
import com.sns.api.users.domain.dto.UserDeleteRequestDto;
import com.sns.api.users.domain.dto.UsersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsersService {

    UsersResponseDto getMyInfo(Long id);

    UsersResponseDto updateMyInfo(Long id, UserUpdateRequestDto dto);

    UserReadResponseDto findById(Long id);

    Page<UserReadResponseDto> searchUsers(Pageable pageable, String username, String email, Long userId);

    void deleteMe(Long id, UserDeleteRequestDto requestDto);

    void updatePassword(Long id, PasswordUpdateDto updateDto);
}