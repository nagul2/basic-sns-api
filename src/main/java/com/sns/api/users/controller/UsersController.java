package com.sns.api.users.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.dto.UpdateUserRequestDto;
import com.sns.api.users.domain.dto.UsersResponseDto;
import com.sns.api.users.service.UsersService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/me")
    public BaseResponse<UsersResponseDto> getMyInfo(@SessionAttribute(Const.LOGIN_USER) UserBaseDto dto) {

        return BaseResponse.success(usersService.getMyInfo(dto.getUserId()), ResultCode.OK);
    }

    @PutMapping("/me")
    public BaseResponse<UsersResponseDto> updateMyInfo(@SessionAttribute(Const.LOGIN_USER) UserBaseDto userDto,
                                                       @RequestBody UpdateUserRequestDto updateDto) {

        return BaseResponse.success(usersService.updateMyInfo(userDto.getUserId(), updateDto), ResultCode.OK);
    }
}