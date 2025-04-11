package com.sns.api.comments.repository.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.api.comments.domain.dto.response.CommentResponseDto;
import com.sns.api.comments.domain.dto.response.QCommentResponseDto;
import com.sns.api.comments.domain.entity.QComments;
import com.sns.api.common.domain.dto.QUserBaseDto;
import com.sns.api.likes.domain.entity.LikeType;
import com.sns.api.likes.domain.entity.QLikes;
import com.sns.api.posts.domain.entity.QPosts;
import com.sns.api.users.domain.entity.QUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentsQueryRepositoryImpl implements CommentsQueryRepository {

    private final JPAQueryFactory queryFactory;

    // Q 타입 필드 선언
    private final QPosts posts = QPosts.posts;
    private final QComments comments = QComments.comments;
    private final QLikes likes = QLikes.likes;
    
    // createdBy, modifiedBy 가 다를 수 있기 때문에 별도의 alias 설정
    private final QUsers createdUser = new QUsers("createdBy");
    private final QUsers modifiedUser = new QUsers("modifiedBy");

    /**
     * 댓글 전체 조회 (페이징 적용)
     * 
     * @param postId        속한 게시물 ID
     * @param userId        로그인한 회원 정보
     * @param pageable      페이징 객체, 항상 최신순으로 정렬
     * @return              댓글 정보, 좋아요 개수, 로그인한 회원의 좋아요 여부 등의 데이터를 포함
     */
    @Override
    public Page<CommentResponseDto> findAllWithQuery(Long postId, Long userId, Pageable pageable) {

        List<CommentResponseDto> results = queryFactory
                .select(new QCommentResponseDto(
                        comments.id,
                        comments.post.id,
                        new QUserBaseDto(createdUser.id, createdUser.username),
                        new QUserBaseDto(modifiedUser.id, modifiedUser.username),
                        comments.content,
                        getLikesCountSubQuery(),
                        getIsLikedSubQuery(userId),
                        comments.createdAt,
                        comments.modifiedAt
                ))
                .from(comments)
                .join(comments.createdBy, createdUser)
                .join(comments.modifiedBy, modifiedUser)
                .where(comments.post.id.eq(postId))
                .orderBy(comments.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(comments.count())
                .from(comments)
                .where(comments.post.id.eq(postId));

        // 페이징 최적화
        // 필요할 때만 count 쿼리 실행함 (ex. 마지막 페이지면 생략 가능)
        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }


    // 좋아요 개수 서브 쿼리
    private JPQLQuery<Long> getLikesCountSubQuery() {

        return JPAExpressions.select(likes.count())
                .from(likes)
                .where(likes.likeType.eq(LikeType.COMMENT)
                        .and(likes.likeTypeId.eq(comments.id)));
    }

    // 좋아요 여부 서브 쿼리
    private BooleanExpression getIsLikedSubQuery(Long userId) {

        return JPAExpressions.selectOne()
                .from(likes)
                .where(likes.likeType.eq(LikeType.COMMENT)
                        .and(likes.likeTypeId.eq(comments.id))
                        .and(likes.createdBy.id.eq(userId)))
                .exists();
    }

}
