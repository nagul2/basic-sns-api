package com.sns.api.auth.controller;

import com.sns.api.auth.domain.dto.LoginRequestDto;
import com.sns.api.auth.domain.dto.SignupRequestDto;
import com.sns.api.auth.service.AuthService;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.dto.UsersResponseDto;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/login")
    public BaseResponse<UserBaseDto> login(@RequestBody @Valid LoginRequestDto dto, HttpServletRequest request) {
        UserBaseDto userBaseDto = authService.login(dto);

        HttpSession session = request.getSession(); // Session 을 가져온다.

        // Session 에 로그인 회원 정보를 저장한다.
        session.setAttribute(Const.LOGIN_USER, userBaseDto);
        return BaseResponse.success(userBaseDto, ResultCode.OK);
    }
}