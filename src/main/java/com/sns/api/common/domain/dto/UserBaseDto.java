package com.sns.api.common.domain.dto;

import com.sns.api.users.domain.entity.Users;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserBaseDto {

    private Long userId; // user Id

    private String username; // 이름

    public static UserBaseDto fromEntity(Users entity) {
        return UserBaseDto.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .build();
    }
}