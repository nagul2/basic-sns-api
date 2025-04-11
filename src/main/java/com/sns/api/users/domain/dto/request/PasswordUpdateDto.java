package com.sns.api.users.domain.dto.request;

import com.sns.common.annotation.PasswordValid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordUpdateDto {

    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @PasswordValid
    private String newPassword;

}