package com.sns.api.auth.service;

import com.sns.api.auth.domain.dto.LoginRequestDto;
import com.sns.api.auth.domain.dto.SignupRequestDto;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.dto.UsersResponseDto;

public interface AuthService {
    UsersResponseDto signup(SignupRequestDto dto);

    UserBaseDto login(LoginRequestDto dto);
}
