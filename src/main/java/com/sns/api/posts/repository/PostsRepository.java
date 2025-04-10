package com.sns.api.posts.repository;

import com.sns.api.posts.domain.entity.Posts;
import com.sns.api.posts.repository.query.PostQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long>, PostQueryRepository {

}
