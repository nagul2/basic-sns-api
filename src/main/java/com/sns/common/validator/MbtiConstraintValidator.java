package com.sns.common.validator;

import com.sns.common.annotation.DateValid;
import com.sns.common.annotation.MbtiValid;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class MbtiConstraintValidator implements ConstraintValidator<MbtiValid, String> {


    private static final String MBTI_REGEX =
            "(?i)^(?:$|ISTJ|ISFJ|INFJ|INTJ|ISTP|ISFP|INFP|INTP|ESTP|ESFP|ENFP|ENTP|ESTJ|ESFJ|ENFJ|ENTJ)$";

    private Pattern pattern = Pattern.compile(MBTI_REGEX);

    @Override
    public void initialize(MbtiValid constraintAnnotation) {
        // 초기화 로직
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            // null 허용 여부에 따라 true/false 선택
            // @NotNull 과 함께 사용할 경우 null은 별도 검증으로 처리
            return true;
        }
        return pattern.matcher(value).matches();
    }
}