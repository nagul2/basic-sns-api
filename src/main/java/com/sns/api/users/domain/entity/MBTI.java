package com.sns.api.users.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MBTI {
    ISTJ, ISFJ, INFJ, INTJ,
    ISTP, ISFP, INFP, INTP,
    ESTP, ESFP, ENFP, ENTP,
    ESTJ, ESFJ, ENFJ, ENTJ;
}