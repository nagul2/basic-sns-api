package com.sns.api.comments.repository;

import com.sns.api.comments.domain.entity.Comments;
import com.sns.api.comments.repository.query.CommentsQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long>, CommentsQueryRepository {

}
