package com.sns.api.users.controller;

import com.sns.api.common.domain.dto.PageResponseDto;
import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.dto.response.UserReadResponseDto;
import com.sns.api.users.domain.dto.request.UserUpdateRequestDto;
import com.sns.api.users.domain.dto.request.PasswordUpdateDto;
import com.sns.api.users.domain.dto.request.UserDeleteRequestDto;
import com.sns.api.users.domain.dto.response.UsersResponseDto;
import com.sns.api.users.service.UsersService;
import com.sns.common.component.BaseResponse;
import com.sns.common.component.Const;
import com.sns.common.component.ResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    /**
     * 내 정보 조회 API
     *
     * @param dto 로그인 유저 정보
     * @return 내 정보 및 200 OK 응답
     */
    @GetMapping("/me")
    public BaseResponse<UsersResponseDto> getMyInfo(@SessionAttribute(Const.LOGIN_USER) UserBaseDto dto) {

        return BaseResponse.success(usersService.getMyInfo(dto.getUserId()), ResultCode.OK);
    }

    /**
     * 내 정보 수정 API
     *
     * @param userDto   로그인 유저 정보
     * @param updateDto 수정할 정보
     * @return 수정된 내 정보 및 200 OK 응답
     */
    @PutMapping("/me")
    public BaseResponse<UsersResponseDto> updateMyInfo(@SessionAttribute(Const.LOGIN_USER) UserBaseDto userDto,
                                                       @RequestBody @Valid UserUpdateRequestDto updateDto) {

        return BaseResponse.success(usersService.updateMyInfo(userDto.getUserId(), updateDto), ResultCode.OK);
    }

    /**
     * 회원 정보 조회 API
     *
     * @param id 조회할 회원 id
     * @return 조회된 회원 정보 및 200 OK 응답
     */
    @GetMapping("/{id}")
    public BaseResponse<UserReadResponseDto> findById(@PathVariable Long id) {

        return BaseResponse.success(usersService.findById(id), ResultCode.OK);
    }

    /**
     * 회원 검색 API
     *
     * @param pageable    페이징 정보
     * @param username    검색할 회원 이름
     * @param email       검색할 회원 이메일
     * @param userBaseDto 로그인 유저 정보
     * @return 검색된 회원 page 및 200 OK 응답
     */
    @GetMapping
    public BaseResponse<PageResponseDto<UserReadResponseDto>> searchUsers(
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String username, @RequestParam(required = false) String email,
            @SessionAttribute(Const.LOGIN_USER) UserBaseDto userBaseDto) {

        return BaseResponse.success(usersService.searchUsers(pageable, username, email, userBaseDto.getUserId()),
                ResultCode.OK);
    }

    /**
     * 회원 탈퇴 API
     *
     * @param requestDto 비밀번호
     * @param dto        로그인 유저 정보
     * @return 성공 시 세션 초기화 및 204 응답
     */
    @DeleteMapping("/me")
    public BaseResponse<Object> deleteMe(@RequestBody @Valid UserDeleteRequestDto requestDto
            , @SessionAttribute(Const.LOGIN_USER) UserBaseDto dto, HttpServletRequest request) {

        usersService.deleteMe(dto.getUserId(), requestDto);

        // 세션 초기화
        HttpSession session = request.getSession();
        session.invalidate();

        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }

    /**
     * 비밀번호 변경 API
     *
     * @param updateDto 비밀번호 변경 데이터
     * @param dto        로그인 유저 정보
     * @return 성공 시 200 응답
     */
    @PutMapping("/me/password")
    public BaseResponse<Object> updatePassword(@RequestBody @Valid PasswordUpdateDto updateDto
            , @SessionAttribute(Const.LOGIN_USER) UserBaseDto dto) {

        usersService.updatePassword(dto.getUserId(), updateDto);
        return BaseResponse.success(null, ResultCode.NO_CONTENT);
    }
}