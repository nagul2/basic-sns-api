package com.sns.api.likes.repository;

import com.sns.api.likes.domain.entity.LikeType;
import com.sns.api.likes.domain.entity.Likes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByLikeTypeAndLikeTypeIdAndCreatedById(LikeType likeType, Long likeTypeId, Long createdBy);

    Optional<Likes> findByLikeTypeAndLikeTypeIdAndCreatedById(LikeType likeType, Long likeTypeId, Long createdBy);
}