package com.sns.api.users.domain.dto;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.entity.Users;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class UsersResponseDto extends UserBaseDto {

    private String email;

    private LocalDate birth;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedDate;

    public static UsersResponseDto fromEntity(Users entity) {
        return UsersResponseDto.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .birth(entity.getBirth())
                .createdAt(entity.getCreatedAt())
                .modifiedDate(entity.getModifiedAt())
                .build();
    }
}