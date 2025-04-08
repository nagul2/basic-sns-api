package com.sns.api.users.controller;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.dto.UserReadResponseDto;
import com.sns.api.users.domain.dto.UserUpdateRequestDto;
import com.sns.api.users.domain.dto.PasswordUpdateDto;
import com.sns.api.users.domain.dto.UserDeleteRequestDto;
import com.sns.api.users.domain.dto.UsersResponseDto;
import com.sns.api.users.service.UsersService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @PutMapping("/me")
    public BaseResponse<UsersResponseDto> updateMyInfo(@SessionAttribute(Const.LOGIN_USER) UserBaseDto userDto,
                                                       @RequestBody @Valid UserUpdateRequestDto updateDto) {

        return BaseResponse.success(usersService.updateMyInfo(userDto.getUserId(), updateDto), ResultCode.OK);
    }

    @GetMapping("/{id}")
    public BaseResponse<UserReadResponseDto> findById(@PathVariable Long id) {

        return BaseResponse.success(usersService.findById(id), ResultCode.OK);
    }

    @GetMapping
    public BaseResponse<Page<UserReadResponseDto>> searchUsers(
            @PageableDefault(size = 5, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String username, @RequestParam(required = false) String email) {

        return BaseResponse.success(usersService.searchUsers(pageable, username, email), ResultCode.OK);
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