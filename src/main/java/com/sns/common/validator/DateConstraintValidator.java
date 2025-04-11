package com.sns.common.validator;

import com.sns.common.annotation.DateValid;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class DateConstraintValidator implements ConstraintValidator<DateValid, String> {


    // 빈 문자열 허용하거나, 1900~2099년 사이의 yyyy-MM-dd 형식만 통과
    private static final String DATE_REGEX =
            "^(?:$|" +
                    "(19|20)\\d\\d-" +            // 연도: 1900~2099
                    "(0[1-9]|1[0-2])-" +          // 월: 01~12
                    "(0[1-9]|[12]\\d|3[01])" +    // 일: 01~31
                    ")$";

    private Pattern pattern = Pattern.compile(DATE_REGEX);

    @Override
    public void initialize(DateValid constraintAnnotation) {
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