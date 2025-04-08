package com.sns.api.users.service;

import com.sns.api.users.domain.dto.ReadUserResponseDto;
import com.sns.api.users.domain.dto.UpdateUserRequestDto;
import com.sns.api.users.domain.dto.PasswordUpdateDto;
import com.sns.api.users.domain.dto.UserDeleteRequestDto;
import com.sns.api.users.domain.dto.UsersResponseDto;
import java.util.List;

public interface UsersService {

    UsersResponseDto getMyInfo(Long id);

    UsersResponseDto updateMyInfo(Long id, UpdateUserRequestDto dto);

    ReadUserResponseDto findById(Long id);

    List<ReadUserResponseDto> searchUsers(String username, String email);

    void deleteMe(Long id, UserDeleteRequestDto requestDto);

    void updatePassword(Long id, PasswordUpdateDto updateDto);
}