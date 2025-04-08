package com.sns.api.users.service;

import com.sns.api.users.domain.dto.UsersResponseDto;

public interface UsersService {

    UsersResponseDto getMyInfo(Long id);
}
