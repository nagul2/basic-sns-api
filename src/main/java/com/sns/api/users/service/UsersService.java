package com.sns.api.users.service;

import com.sns.api.common.domain.dto.PageResponseDto;
import com.sns.api.users.domain.dto.response.UserReadResponseDto;
import com.sns.api.users.domain.dto.request.UserUpdateRequestDto;
import com.sns.api.users.domain.dto.request.PasswordUpdateDto;
import com.sns.api.users.domain.dto.request.UserDeleteRequestDto;
import com.sns.api.users.domain.dto.response.UsersResponseDto;
import org.springframework.data.domain.Pageable;

public interface UsersService {

    UsersResponseDto getMyInfo(Long id);

    UsersResponseDto updateMyInfo(Long id, UserUpdateRequestDto dto);

    UserReadResponseDto findById(Long id);

    PageResponseDto<UserReadResponseDto> searchUsers(Pageable pageable, String username, String email, Long userId);

    void deleteMe(Long id, UserDeleteRequestDto requestDto);

    void updatePassword(Long id, PasswordUpdateDto updateDto);
}