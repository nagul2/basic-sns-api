package com.sns.api.posts.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.api.comments.domain.entity.QComments;
import com.sns.api.common.domain.dto.QUserBaseDto;
import com.sns.api.friends.domain.entity.FriendsStatus;
import com.sns.api.friends.domain.entity.QFriends;
import com.sns.api.likes.domain.entity.LikeType;
import com.sns.api.likes.domain.entity.QLikes;
import com.sns.api.posts.domain.dto.response.PostResponseDto;
import com.sns.api.posts.domain.dto.response.QPostResponseDto;
import com.sns.api.posts.domain.entity.QPosts;
import com.sns.api.users.domain.entity.QUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final QFriends friends = QFriends.friends;

    /**
     * 게시물 단건 조회
     * 
     * @param postId    조회하려는 게시물 ID
     * @param userId    로그인한 회원 정보
     *                  
     * @return  게시물/작성자 정보, 댓글 목록, 댓글 개수, 좋아요 개수, 로그인한 회원의 좋아요 여부 등의 데이터를 포함
     */
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

    /**
     * 게시물 전체 조회 (페이징 적용)
     * - 클라이언트가 요청한 정렬 조건에 맞춰 데이터 조회
     * - startDate, endDate 데이터가 유효하지 않으면 기간 전체 범위로 검색
     *
     * @param userId        로그인한 회원 ID
     * @param startDate     생성일 기간별 검색 조건 (시작일)
     * @param endDate       생성일 기간별 검색 조건 (종료일)
     * @param pageable      page, size, 정렬 조건 등을 가지고 있는 페이징 객체
     *
     * @return  게시물/작성자 정보, 댓글 개수, 좋아요 개수, 로그인한 회원의 좋아요 여부 등의 데이터를 포함
     */
    @Override
    public Page<PostResponseDto> findAllWithQuery(Long userId,
                                                  LocalDateTime startDate,
                                                  LocalDateTime endDate,
                                                  Pageable pageable) {

        // 동적 where 조건 빌더
        BooleanBuilder builder = new BooleanBuilder();

        // 생성일 기준 기간별 검색
        if (startDate != null && endDate != null) {
            builder.and(posts.createdAt.goe(startDate));
            builder.and(posts.createdAt.loe(endDate));
        }

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
                .orderBy(getOrderSpecifiers(pageable.getSort(), userId))
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


    // 정렬 기준 동적 생성
    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort, Long userId) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        orders.add(getFriendPriority(userId).asc());
        orders.add(
                new OrderSpecifier<>(
                        Order.DESC,
                        new CaseBuilder()
                                .when(getFriendCondition(userId)).then(posts.createdAt)
                                .otherwise((DateTimeExpression<LocalDateTime>) null)
                )
        );

        // 사용자가 요청한 정렬
        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;     // 정렬 방향

            switch (order.getProperty()) {
                case "comment" -> orders.add(new OrderSpecifier<>(direction, getCommentCountSubQuery()));   // 댓글 기준 정렬
                case "like" -> orders.add(new OrderSpecifier<>(direction, getLikesCountSubQuery()));        // 좋아요 기준 정렬
                case "createdAt" -> orders.add(new OrderSpecifier<>(direction, posts.createdAt));           // 생성일 기준 정렬
                case "modifiedAt" -> orders.add(new OrderSpecifier<>(direction, posts.modifiedAt));         // 수정일 기준 정렬
                default -> orders.add(posts.createdAt.desc());                                              // 기본 (생성일 기준 내림차순)
            }
        }

        // List -> 배열로 바꾸는 자바의 일반적인 패턴
        // new T[0]과 같은 빈 배열을 넘기면, 자바가 그 타입을 기준으로 적절한 크기의 배열을 만을어서 반환한다.
        return orders.toArray(new OrderSpecifier[0]);
    }

    private NumberExpression<Integer> getFriendPriority(Long userId) {

        return new CaseBuilder()
                .when(getFriendCondition(userId)).then(0)
                .otherwise(1);
    }

    private BooleanExpression getFriendCondition(Long userId) {

        return JPAExpressions
                .selectOne()
                .from(friends)
                .where(
                        friends.status.eq(FriendsStatus.ACCEPT)
                                .and(
                                        friends.fromUser.id.eq(userId).and(friends.toUser.id.eq(posts.createdBy.id))
                                                .or(friends.toUser.id.eq(userId).and(friends.fromUser.id.eq(posts.createdBy.id)))
                                )
                )
                .exists();
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

    // 좋아요 여부 서브 쿼리
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
