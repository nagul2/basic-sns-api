package com.sns.api.posts.domain.entity;

import com.sns.api.common.domain.entity.BaseUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "posts", indexes = @Index(name = "idx_created_at", columnList = "created_at"))
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE posts SET is_deleted = true WHERE id = ?")     // soft delete 적용
public class Posts extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // PK

    @Column(nullable = false)
    private String content;     // 게시물 본문

    @Column(nullable = false)
    private Boolean isDeleted;  // 삭제 여부

    public static Posts of(String content) {
        return new Posts(content);
    }

    public Posts(String content) {
        this.content = content;

        this.isDeleted = false;
    }

    public void updateContent(String content) {
        this.content = content;
    }

}
