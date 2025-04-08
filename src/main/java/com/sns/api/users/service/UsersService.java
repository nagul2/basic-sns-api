package com.sns.api.users.service;

import com.sns.api.users.domain.dto.UserReadResponseDto;
import com.sns.api.users.domain.dto.UserUpdateRequestDto;
import com.sns.api.users.domain.dto.PasswordUpdateDto;
import com.sns.api.users.domain.dto.UserDeleteRequestDto;
import com.sns.api.users.domain.dto.UsersResponseDto;
import java.util.List;

public interface UsersService {

    UsersResponseDto getMyInfo(Long id);

    UsersResponseDto updateMyInfo(Long id, UserUpdateRequestDto dto);

    UserReadResponseDto findById(Long id);

    List<UserReadResponseDto> searchUsers(String username, String email);

    void deleteMe(Long id, UserDeleteRequestDto requestDto);

    void updatePassword(Long id, PasswordUpdateDto updateDto);
}