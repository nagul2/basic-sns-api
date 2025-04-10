package com.sns.api.follow.repository;

import com.sns.api.follow.domain.entity.Follows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowsRepository extends JpaRepository<Follows, Long> {

    /**
     * 로그인 유저 Id, 팔로우 대상 ID를 기준으로 팔로워 데이터를 조회하는 쿼리
     *
     * @param followerId 로그인 유저 Id
     * @param followingId 팔로우 대상 정보
     * @return 조회된 팔로워 데이터 반환(페이징 적용)
     */
    Optional<Follows> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * 로그인 유저 Id를 기준으로 팔로워 데이터를 조회하는 쿼리
     * @EntityGraph 사용하여 연관된 follower 조회
     *
     * @param followingId 로그인 유저 Id
     * @param pageable 페이징 정보
     * @return 조회된 팔로워 데이터 반환(페이징 적용)
     */
    @EntityGraph(attributePaths = {"follower"})
    @Query("select f from Follows f where f.following.id = :id and f.follower.isDeleted = false ORDER BY f.follower.username asc")
    Page<Follows> findAllByFollowingIdWithActiveMember(@Param("id") Long followingId, Pageable pageable);

    /**
     * 로그인 유저 Id를 기준으로 팔로잉 데이터를 조회하는 쿼리
     * @EntityGraph 사용하여 연관된 following 조회
     *
     * @param followerId 로그인 유저 Id
     * @param pageable 페이징 정보
     * @return 조회된 팔로잉 데이터 반환(페이징 적용)
     */
    @EntityGraph(attributePaths = {"following"})
    @Query("select f from Follows f join f.following m where f.follower.id = :id and m.isDeleted = false ORDER BY m.username asc")
    Page<Follows> findAllByFollowerIdWithActiveMember(@Param("id") Long followerId, Pageable pageable);
}
