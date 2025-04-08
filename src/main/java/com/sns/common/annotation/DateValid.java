package com.sns.common.annotation;

import com.sns.common.validator.DateConstraintValidator;
import com.sns.common.validator.PasswordConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DateConstraintValidator.class)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface DateValid {

    // 기본 에러 메시지
    String message() default "날짜 형식은 yyyy-MM-dd 이어야 합니다.";

    // 그룹핑 용도 (기본적으로 비워둡니다)
    Class<?>[] groups() default {};

    // 페이로드 용도 (추가 메타정보 전달 시 사용)
    Class<? extends Payload>[] payload() default {};
}