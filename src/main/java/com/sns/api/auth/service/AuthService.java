package com.sns.api.auth.service;

import com.sns.api.auth.domain.dto.request.LoginRequestDto;
import com.sns.api.auth.domain.dto.request.SignupRequestDto;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.dto.response.UsersResponseDto;

public interface AuthService {
    UsersResponseDto signup(SignupRequestDto dto);

    UserBaseDto login(LoginRequestDto dto);
}
