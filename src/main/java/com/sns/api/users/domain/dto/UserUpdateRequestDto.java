package com.sns.api.users.domain.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UserUpdateRequestDto {

    @Length(max = 10, message = "이름은 10글자까지 입력가능합니다.")
    private String username;

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
