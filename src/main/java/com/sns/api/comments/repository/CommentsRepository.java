package com.sns.api.comments.repository;

import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.posts.domain.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    Page<Comments> findAllByPost(Posts post, Pageable pageable);

}
