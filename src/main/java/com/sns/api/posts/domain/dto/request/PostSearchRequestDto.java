package com.sns.api.posts.domain.dto.request;

import com.sns.common.annotation.DateValid;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Getter
@AllArgsConstructor
public class PostSearchRequestDto {

    @DateValid
    private String startDate;

    @DateValid
    private String endDate;

    /**
     * 두 가지 항목을 검사한다.
     * 1. startDate <= endDate 인지
     * 2. startDate, endDate 두 값 모두 null 아닌지
     * @return 데이터 유효성 판단
     */
    public boolean hasValidValue() {

        // 두 값 모두 null 아니어야 함
        if (startDate == null || endDate == null) {
            return false;
        }

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            return !start.isAfter(end);     // start < end
        } catch (DateTimeParseException e) {
            return false;   // 형식 자체가 잘못되었을 경우도 무효
        }
    }

    public LocalDateTime getStartDate() {
        return LocalDate.parse(startDate).atStartOfDay();
    }

    public LocalDateTime getEndDate() {
        // 2025-04-08 까지의 데이터를 조회하고 싶을 때,
        // endDate = 2025-04-08이면 2025-04-08 00:00:00 으로 변환됨
        // 즉, 2025-04-07 23:59:59 까지의 데이터만 조회하게 되기 때문에 plusDays(1)을 해줘야 함
        return LocalDate.parse(endDate).plusDays(1).atStartOfDay();
    }

}
