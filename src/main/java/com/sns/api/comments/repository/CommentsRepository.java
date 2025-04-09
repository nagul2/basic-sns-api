package com.sns.api.comments.repository;

import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.posts.domain.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    /**
     * 
     * @param post 게시물 entity
     * @param pageable 생성일 기준 내림차순 정렬이 default
     * @return
     */
    @EntityGraph(attributePaths = "createdBy")
    Page<Comments> findAllByPost(Posts post, Pageable pageable);

}
