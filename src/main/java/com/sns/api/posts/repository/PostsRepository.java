package com.sns.api.posts.repository;

import com.sns.api.posts.domain.dto.response.PostFlatDto;
import com.sns.api.posts.domain.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    /**
     * 게시글 전체 조회
     * 댓글 개수를 서브쿼리를 날려서 카운트
     *
     * @param pageable 생성일 기준 내림차순 정렬이 디폴트
     * @return PostFlatDto DTO 객체에 매핑 후 반환
     */
    @Query(value = """
        SELECT new com.sns.api.posts.domain.dto.response.PostFlatDto(
            p.id,
            u.id,
            u.username,
            p.content,
            (SELECT CAST(count(c) as long) FROM Comments c WHERE c.post.id = p.id),
            p.createdAt,
            p.modifiedAt
        )
        FROM Posts p
        JOIN p.createdBy u
    """,
    countQuery = "SELECT COUNT(p) FROM Posts p")
    Page<PostFlatDto> findAllWithCommentCount(Pageable pageable);

    // TODO: 위의 쿼리와 중복된다. Querydsl 등을 활용하여 동적으로 쿼리를 사용해보자.
    @Query(value = """
        SELECT new com.sns.api.posts.domain.dto.response.PostFlatDto(
            p.id,
            u.id,
            u.username,
            p.content,
            (SELECT CAST(count(c) as long) FROM Comments c WHERE c.post.id = p.id),
            p.createdAt,
            p.modifiedAt
        )
        FROM Posts p
        JOIN p.createdBy u
        WHERE p.createdAt >= :startDate AND p.createdAt <= :endDate
    """,
    countQuery = "SELECT COUNT(p) FROM Posts p")
    Page<PostFlatDto> findPostsByCreatedAt(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate,
                                     Pageable pageable);

}
