package com.sns.api.users.domain.dto;

import com.sns.api.users.domain.entity.Users;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ReadUserResponseDto {

    private Long userId;

    private String email;

    private String username;

    private LocalDate birth;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public static ReadUserResponseDto fromEntity(Users user) {
        return ReadUserResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .birth(user.getBirth())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
    }
}
