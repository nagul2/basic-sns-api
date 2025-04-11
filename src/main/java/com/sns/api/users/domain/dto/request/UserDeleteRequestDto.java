package com.sns.api.users.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class UserDeleteRequestDto{
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}