package com.sns.common.annotation;

import com.sns.common.validator.PasswordConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface PasswordValid {

    // 기본 에러 메시지
    String message() default "비밀번호는 8~20자 사이여야 하며, 소문자, 대문자, 숫자, 특수문자를 각각 최소 한 개 이상 포함해야 합니다.";

    // 그룹핑 용도 (기본적으로 비워둡니다)
    Class<?>[] groups() default {};

    // 페이로드 용도 (추가 메타정보 전달 시 사용)
    Class<? extends Payload>[] payload() default {};
}