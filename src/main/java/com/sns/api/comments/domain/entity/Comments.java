package com.sns.api.comments.domain.entity;

import com.sns.api.common.domain.entity.BaseUserEntity;
import com.sns.api.posts.domain.entity.Posts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "comments")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE comments SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Comments extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // PK

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;         // 소속 게시물
    
    @Column(nullable = false)
    private String content;     // 댓글 본문

    @Column(nullable = false)
    private Boolean isDeleted;  // 삭제 여부

    public static Comments of(Posts post, String content) {
        return new Comments(post, content);
    }

    public Comments(Posts post, String content) {
        this.post = post;
        this.content = content;
        this.isDeleted = false;
    }

    public void updateComment(String content) {
        this.content = content;
    }

}
