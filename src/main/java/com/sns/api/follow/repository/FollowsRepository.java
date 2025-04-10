package com.sns.api.follow.repository;

import com.sns.api.follow.domain.entity.Follows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowsRepository extends JpaRepository<Follows, Long> {
    Optional<Follows> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @EntityGraph(attributePaths = {"follower"})
    @Query("select f from Follows f where f.following.id = :id and f.follower.isDeleted = false ORDER BY f.follower.username asc")
    Page<Follows> findAllByFollowingIdWithActiveMember(@Param("id") Long followingId, Pageable pageable);

    @EntityGraph(attributePaths = {"following"})
    @Query("select f from Follows f join f.following m where f.follower.id = :id and m.isDeleted = false ORDER BY m.username asc")
    Page<Follows> findAllByFollowerIdWithActiveMember(@Param("id") Long followerId, Pageable pageable);
}
