package com.sns.api.users.domain.dto;

import com.sns.api.common.domain.dto.UserBaseDto;
import com.sns.api.users.domain.entity.MBTI;
import com.sns.api.users.domain.entity.Users;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class UsersResponseDto extends UserBaseDto {

    private String email; // 이메일

    private LocalDate birth; // 생년월일

    private MBTI mbti; // mbti

    private LocalDateTime createdAt; // 생성일

    private LocalDateTime modifiedDate; // 수정일

    public static UsersResponseDto fromEntity(Users entity) {
        return UsersResponseDto.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .birth(entity.getBirth())
                .mbti(entity.getMbti())
                .createdAt(entity.getCreatedAt())
                .modifiedDate(entity.getModifiedAt())
                .build();
    }
}