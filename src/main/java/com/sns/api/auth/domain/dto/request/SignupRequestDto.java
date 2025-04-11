package com.sns.api.auth.domain.dto.request;

import com.sns.common.annotation.DateValid;
import com.sns.common.annotation.MbtiValid;
import com.sns.common.annotation.PasswordValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효하지 않는 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "회원 이름을 입력해주세요.")
    @Length(max = 10, message = "이름은 10글자까지 입력가능합니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @PasswordValid
    private String password;

    @DateValid(message = "생년월일은 yyyy-MM-dd 형식이어야 합니다.")
    private String birth;

    @MbtiValid
    private String mbti;
}
