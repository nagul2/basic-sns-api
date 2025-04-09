package com.sns.api.friends.domain.dto.request;

import com.sns.api.friends.domain.entity.FriendsStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActionFriendsRequestDto {

    @NotNull(message = "status는 필수값 입니다.")
    private final FriendsStatus status;
}
