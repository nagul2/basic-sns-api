package com.sns.api.users.service;

import com.sns.api.users.domain.dto.UpdateUserRequestDto;
import com.sns.api.users.domain.dto.UsersResponseDto;

public interface UsersService {

    UsersResponseDto getMyInfo(Long id);

    UsersResponseDto updateMyInfo(Long id, UpdateUserRequestDto dto);
}