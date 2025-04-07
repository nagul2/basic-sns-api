package com.sns.api.users.domain.entity;


import com.sns.api.common.domain.entity.BaseTimeEntity;
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
    private LocalDate birth; // 생년월일

    @Column(nullable = false)
    private Boolean isDeleted; // 삭제여부
}