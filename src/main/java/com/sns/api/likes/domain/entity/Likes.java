package com.sns.api.likes.domain.entity;

import com.sns.api.common.domain.entity.BaseUserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor
public class Likes extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LikeType likeType;

    private Long likeTypeId;

    public Likes(LikeType likeType, Long likeTypeId) {
        this.likeType = likeType;
        this.likeTypeId = likeTypeId;
    }
}
