package com.sns.api.users.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.dto.PasswordUpdateDto;
import com.sns.api.users.domain.dto.UserDeleteRequestDto;
import com.sns.api.users.domain.dto.UsersResponseDto;
import com.sns.api.users.service.UsersService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/me")
    public BaseResponse<UsersResponseDto> getMyInfo(@SessionAttribute(Const.LOGIN_USER) UserBaseDto dto) {

        return BaseResponse.success(usersService.getMyInfo(dto.getUserId()), ResultCode.OK);
    }

    @DeleteMapping("/me")
    public BaseResponse<Object> deleteMe(@RequestBody @Valid UserDeleteRequestDto requestDto
            , @SessionAttribute(Const.LOGIN_USER) UserBaseDto dto, HttpServletRequest request) {

        usersService.deleteMe(dto.getUserId(), requestDto);

        // 세션 초기화
        HttpSession session = request.getSession();
        session.invalidate();

        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }

    @PutMapping("/me/password")
    public BaseResponse<Object> updatePassword(@RequestBody @Valid PasswordUpdateDto updateDto
            , @SessionAttribute(Const.LOGIN_USER) UserBaseDto dto) {

        usersService.updatePassword(dto.getUserId(), updateDto);
        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }
}