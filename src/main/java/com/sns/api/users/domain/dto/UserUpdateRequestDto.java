package com.sns.api.users.domain.dto;

import com.sns.common.annotation.DateValid;
import com.sns.common.annotation.MbtiValid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UserUpdateRequestDto {

    @NotBlank(message = "회원 이름을 입력해주세요.")
    @Length(max = 10, message = "이름은 10글자까지 입력가능합니다.")
    private String username;

    @DateValid
    private String birth;

    @MbtiValid
    private String mbti;
}
