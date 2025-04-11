package com.sns.api.auth.controller;

import com.sns.api.auth.domain.dto.request.LoginRequestDto;
import com.sns.api.auth.domain.dto.request.SignupRequestDto;
import com.sns.api.auth.service.AuthService;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.dto.response.UsersResponseDto;
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


    /**
     * 회원 가입 API
     *
     * @param dto 회원 가입 요청 Data
     * @return 성공 시 UsersResponseDto 및 201 응답
     */
    @PostMapping("/signup")
    public BaseResponse<UsersResponseDto> signup(@RequestBody @Valid SignupRequestDto dto) {
        return BaseResponse.success(authService.signup(dto), ResultCode.CREATED);
    }

    /**
     * 로그인 API
     *
     * @param dto 로그인 요청 Data
     * @return 성공 시 UserBaseDto 및 200 응답
     */
    @PostMapping("/login")
    public BaseResponse<UserBaseDto> login(@RequestBody @Valid LoginRequestDto dto, HttpServletRequest request) {
        UserBaseDto userBaseDto = authService.login(dto);

        HttpSession session = request.getSession(); // Session 을 가져온다.

        // Session 에 로그인 회원 정보를 저장한다.
        session.setAttribute(Const.LOGIN_USER, userBaseDto);
        return BaseResponse.success(userBaseDto, ResultCode.OK);
    }

    /**
     * 로그인 API
     *
     * @return 성공 시 세션 초기화 및 204 응답
     */
    @PostMapping("/logout")
    public BaseResponse<Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 미 로그인 시 null 반환
        if (session != null) {
            session.invalidate(); // 해당 세션(데이터)을 삭제한다.
        }
        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }
}