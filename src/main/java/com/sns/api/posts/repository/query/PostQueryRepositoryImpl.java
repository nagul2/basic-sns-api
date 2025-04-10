package com.sns.api.posts.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.api.comments.domain.entity.QComments;
import com.sns.api.common.domain.dto.QUserBaseDto;
import com.sns.api.likes.domain.entity.LikeType;
import com.sns.api.likes.domain.entity.QLikes;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import com.sns.api.posts.domain.dto.response.QPostResponseDto;
import com.sns.api.posts.domain.entity.QPosts;
import com.sns.api.users.domain.entity.QUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    private final QPosts posts = QPosts.posts;
    private final QUsers users = QUsers.users;
    private final QComments comments = QComments.comments;
    private final QLikes likes = QLikes.likes;

    @Override
    public Optional<PostResponseDto> findPostWithQuery(Long postId, Long userId) {

        return Optional.ofNullable(
                queryFactory
                        .select(new QPostResponseDto(
                                posts.id,
                                new QUserBaseDto(
                                        users.id,
                                        users.username
                                ),
                                posts.content,
                                getCommentCountSubQuery(),
                                getLikesCountSubQuery(),
                                getIsLikedSubQuery(userId),
                                posts.createdAt,
                                posts.modifiedAt
                        ))
                        .from(posts)
                        .join(posts.createdBy, users)
                        .where(posts.id.eq(postId))
                        .fetchOne()
        );
    }

    @Override
    public Page<PostResponseDto> findAllWithQuery(Long userId,
                                                  LocalDateTime startDate,
                                                  LocalDateTime endDate,
                                                  Pageable pageable) {

        // 정렬 조건 동적 분기
        OrderSpecifier<?> orderSpecifier = switch (pageable.getSort().toString().split(":")[0]) {
            case "comment" -> new OrderSpecifier<>(Order.DESC, getCommentCountSubQuery());   // 댓글 많은 순
            case "like" -> new OrderSpecifier<>(Order.DESC, getLikesCountSubQuery());        // 좋아요 많은 순
            case "modifiedAt" -> new OrderSpecifier<>(Order.DESC, posts.modifiedAt);         // 수정일 기준 내림차순
            default -> posts.createdAt.desc();                                               // 기본 (생성일 기준 내림차순)
        };

        // 동적 where 조건 빌더
        BooleanBuilder builder = new BooleanBuilder();
        
        // 생성일 기준 기간별 검색
        if (startDate != null && endDate != null) {
            builder.and(posts.createdAt.goe(startDate));
            builder.and(posts.createdAt.loe(endDate));
        }
        
        // 본문 키워드 검색
        // TODO: 지금하면 덩치가 커지니 나중에 하자
//        if (keyword != null && !keyword.isBlank()) {
//            builder.and(posts.content.containsIgnoreCase(keyword));
//        }

        // 메인 쿼리
        List<PostResponseDto> results = queryFactory
                .select(new QPostResponseDto(
                        posts.id,
                        new QUserBaseDto(
                                users.id,
                                users.username
                        ),
                        posts.content,
                        getCommentCountSubQuery(),
                        getLikesCountSubQuery(),
                        getIsLikedSubQuery(userId),
                        posts.createdAt,
                        posts.modifiedAt
                ))
                .from(posts)
                .join(posts.createdBy, users)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(posts.count())
                .from(posts)
                .where(builder);

        // 페이징 최적화
        // 필요할 때만 count 쿼리 실행함 (ex. 마지막 페이지면 생략 가능)
        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    // 댓글 개수 서브 쿼리
    private JPQLQuery<Long> getCommentCountSubQuery() {
        return JPAExpressions.select(comments.count())
                .from(comments)
                .where(comments.post.eq(posts));
    }

    // 좋아요 개수 서브 쿼리
    private JPQLQuery<Long> getLikesCountSubQuery() {
        return JPAExpressions.select(likes.count())
                .from(likes)
                .where(likeCountCondition(likes, posts));
    }

    private BooleanExpression getIsLikedSubQuery(Long userId) {
        return JPAExpressions.selectOne()
                .from(likes)
                .where(likedByMeCondition(likes, userId))
                .exists();
    }

    private BooleanExpression likeCountCondition(QLikes likes, QPosts posts) {
        return likes.likeType.eq(LikeType.POST)
                .and(likes.likeTypeId.eq(posts.id));
    }

    private BooleanExpression likedByMeCondition(QLikes likes, Long userId) {
        return likes.likeType.eq(LikeType.POST)
                .and(likes.likeTypeId.eq(posts.id))
                .and(likes.createdBy.id.eq(userId));
    }

}
