package com.sns.api.friends.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendFriendsRequestDto {

    @NotNull(message = "receiverId는 필수입니다.")
    private final Long receiverId;
}
