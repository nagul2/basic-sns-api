package com.sns.api.comments.repository;

import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.posts.domain.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    /**
     * 특정 게시물에 속하는 댓글 전체 조회
     * 
     * @param post 게시물 entity
     * @param pageable 생성일 기준 내림차순 정렬이 디폴트
     * @return 댓글 목록
     */
    @EntityGraph(attributePaths = "createdBy")
    Page<Comments> findAllByPost_Id(Long postId, Pageable pageable);

}
