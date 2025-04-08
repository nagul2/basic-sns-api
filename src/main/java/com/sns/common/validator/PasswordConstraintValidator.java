package com.sns.common.validator;

import com.sns.common.annotation.PasswordValid;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordConstraintValidator implements ConstraintValidator<PasswordValid, String> {

    // 원래 사용하시던 정규식
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])" +         // 소문자 최소 1개
                    "(?=.*[A-Z])" +          // 대문자 최소 1개
                    "(?=.*\\d)" +            // 숫자 최소 1개
                    "(?=.*[!@#$%^&*(),.?\":{}|<>])" + // 특수문자 최소 1개
                    "[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,20}$"; // 전체 길이 8~20

    private Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    @Override
    public void initialize(PasswordValid constraintAnnotation) {
        // 초기화 로직
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(password)) {
            // null 허용 여부에 따라 true/false 선택
            // 만약 @NotNull 과 함께 쓸 거면 null일 땐 true 반환
            return true;
        }
        return pattern.matcher(password).matches();
    }
}