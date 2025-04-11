package com.sns.api.likes.domain.entity;

import com.sns.api.users.domain.entity.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"like_type", "like_type_id", "created_by"}))
@Getter
@NoArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_type", nullable = false)
    private LikeType likeType;

    @Column(name = "like_type_id", nullable = false)
    private Long likeTypeId;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false, nullable = false)
    private Users createdBy;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Likes(LikeType likeType, Long likeTypeId) {
        this.likeType = likeType;
        this.likeTypeId = likeTypeId;
    }

    // Dummy 데이터용 setter
    public void setOnlyDummyCreatedBy(Users user) {
        this.createdBy = user;
    }
}
