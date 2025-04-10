package com.sns.api.follow.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FollowsRequestDto {

    @NotNull(message = "targetId는 필수입니다.")
    private Long targetId;
}
