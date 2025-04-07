package com.sns.api.common.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 해당 추상 클래스를 상속할 경우 createDate, modifiedDate 를 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class)
abstract public class BaseTimeEntity {

    // 생성 시 자동 저장
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 수정 시 자동 저장
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
