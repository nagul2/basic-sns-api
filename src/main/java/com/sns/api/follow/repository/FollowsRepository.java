package com.sns.api.follow.repository;

import com.sns.api.follow.domain.entity.Follows;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowsRepository extends JpaRepository<Follows, Long> {
    Optional<Follows> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
