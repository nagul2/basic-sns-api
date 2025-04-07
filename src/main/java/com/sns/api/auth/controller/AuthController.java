package com.sns.api.auth.controller;

import com.sns.api.auth.domain.dto.SignupRequestDto;
import com.sns.api.auth.service.AuthService;
import com.sns.api.users.domain.dto.UsersResponseDto;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public BaseResponse<UsersResponseDto> signup(@RequestBody @Valid SignupRequestDto dto) {
        return BaseResponse.success(authService.signup(dto), ResultCode.CREATED);
    }
}