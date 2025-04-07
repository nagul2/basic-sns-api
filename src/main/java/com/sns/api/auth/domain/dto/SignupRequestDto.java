package com.sns.api.auth.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,20}$",
            message = "비밀번호는 8~20자 사이여야 하며, 소문자, 대문자, 숫자, 특수문자를 각각 최소 한 개 이상 포함해야 합니다."
    )
    private String password;

    @Pattern(
            regexp = "^(?:$|(19|20)\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$",
            message = "생년월일은 yyyy-MM-dd 형식이어야 합니다."
    )
    private String birth;

    @Pattern(
            regexp = "^(?:$|ISTJ|ISFJ|INFJ|INTJ|ISTP|ISFP|INFP|INTP|ESTP|ESFP|ENFP|ENTP|ESTJ|ESFJ|ENFJ|ENTJ)$",
            message = "유효한 MBTI 형식이어야 합니다."
    )
    private String mbti;
}
