package com.sns.api.users.domain.entity;


import com.sns.api.common.domain.entity.BaseTimeEntity;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "users")
@SQLRestriction("is_deleted = false")
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private String username; // 이름

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column
    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    @Column
    private LocalDate birth; // 생년월일

    @Column(nullable = false)
    private Boolean isDeleted; // 삭제여부

    public Users() {
    }

    public Users (String email, String username, String password, String birth, String mbti) {
        this.email = email;
        this.username = username;
        this.password = password;

        if (StringUtils.isNotEmpty(birth)) { // birth 가 입력되었을 경우
            this.birth = LocalDate.parse(birth);
        }

        if (StringUtils.isNotEmpty(mbti)) { // mbti 가 입력되었을 경우
            this.mbti = MBTI.valueOf(mbti.toUpperCase());
        }

        this.isDeleted = false;
    }
}