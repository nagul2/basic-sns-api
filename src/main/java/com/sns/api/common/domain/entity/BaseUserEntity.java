package com.sns.api.common.domain.entity;

import com.sns.api.users.domain.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract public class BaseUserEntity extends BaseTimeEntity{

    // 생성자 ID
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false, nullable = false)
    private Users createdBy;

    // 수정자 ID
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    private Users modifiedBy;

    // Dummy 데이터용 setter
    public void setOnlyDummyCreatedBy(Users user) {
        this.createdBy = user;
    }

    public void setOnlyDummyModifiedBy(Users user) {
        this.modifiedBy = user;
    }
}
