package com.sns.api.follow.repository;

import com.sns.api.follow.domain.entity.Follows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowsRepository extends JpaRepository<Follows, Long> {
    Optional<Follows> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("select f from Follows f join f.follower m where f.following.id = :id and m.isDeleted = false")
    List<Follows> findAllByFollowingIdWithActiveMember(@Param("id") Long followingId);
}
