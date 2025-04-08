package com.sns.api.users.service.impl;

import com.sns.api.users.domain.dto.UsersResponseDto;
import com.sns.api.users.domain.entity.Users;

import com.sns.api.users.repository.UsersRepository;
import com.sns.api.users.service.UsersService;
import com.sns.common.component.ResultCode;
import com.sns.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public UsersResponseDto getMyInfo(Long id) {

        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.NOT_FOUND));

        return UsersResponseDto.fromEntity(user);
    }
}