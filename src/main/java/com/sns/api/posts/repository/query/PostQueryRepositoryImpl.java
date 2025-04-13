package com.sns.api.posts.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
import com.sns.api.posts.domain.dto.request.PostSearchCondition;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    // Q 타입 필드 선언
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
     * @param userId            로그인한 회원 ID
     * @param searchCondition   검색 조건
     * @param pageable          page, size, 정렬 조건 등을 가지고 있는 페이징 객체
     *
     * @return  게시물/작성자 정보, 댓글 개수, 좋아요 개수, 로그인한 회원의 좋아요 여부 등의 데이터를 포함
     */
    @Override
    public Page<PostResponseDto> findAllWithQuery(Long userId,
                                                  PostSearchCondition searchCondition,
                                                  Pageable pageable) {

        // count 쿼리 작성 (페이징 total count 용도)
        JPAQuery<Long> countQuery = queryFactory.select(posts.count()).from(posts);

        // 메인 쿼리
        JPAQuery<PostResponseDto> query = queryFactory
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
                .join(posts.createdBy, users);

        // 전체 게시물을 조회할지, 친구 게시물만 조회할지, 그 외 사람들의 게시물만 조회할지에 따라 (isOnlyFriends)
        // 메인 쿼리와 카운트 쿼리의 JOIN 구문이 분기 처리된다.
        // 차례대로 LEFT JOIN, INNER JOIN, LEFT OUT JOIN
        if (searchCondition.getIsOnlyFriends() == null) {                   // 전체 게시물 조회
            query
                    .leftJoin(friends)
                    .on(getFriendCondition(userId));

            countQuery
                    .leftJoin(friends)
                    .on(getFriendCondition(userId))
                    .where(getWhereCondition(searchCondition));
        } else if (searchCondition.getIsOnlyFriends() == Boolean.TRUE) {    // 친구 게시물만 조회하려는 경우
            query
                    .innerJoin(friends)
                    .on(getFriendCondition(userId));

            countQuery
                    .innerJoin(friends)
                    .on(getFriendCondition(userId));
        } else {                                                            // 그 외 사람들의 게시물만 조회하려는 경우
            query
                    .leftJoin(friends)
                    .on(getFriendCondition(userId))
                    .where(friends.id.isNull());

            countQuery
                    .leftJoin(friends)
                    .on(getFriendCondition(userId))
                    .where(friends.id.isNull());
        }

        // 쿼리 마무리
        query
                .where(getWhereCondition(searchCondition))     // 동적 where 조건 적용
                .orderBy(getOrderSpecifiers(searchCondition, pageable.getSort()))    // 정렬 조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        countQuery
                .where(getWhereCondition(searchCondition));

        // 페이징 최적화
        // 필요할 때만 count 쿼리 실행함 (ex. 마지막 페이지면 생략 가능)
        return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchOne);
    }

    @Override
    public Page<PostResponseDto> findMyLikedPosts(Long userId, Pageable pageable) {

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
                .join(likes).on(likes.likeTypeId.eq(posts.id))
                .where(likes.createdBy.id.eq(userId))
                .orderBy(posts.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(posts.count())
                .from(posts)
                .join(likes).on(likes.likeTypeId.eq(posts.id))
                .where(likes.createdBy.id.eq(userId), likes.likeType.eq(LikeType.POST));

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }


    private BooleanBuilder getWhereCondition(PostSearchCondition searchCondition) {

        // 동적 where 조건 빌더
        // 추후 이를 활용해 게시물 본문 등을 키워드로 검색할 수도 있다.
        BooleanBuilder builder = new BooleanBuilder();

        // 생성일 기준 기간별 검색
        if (searchCondition.hasValidValue()) {
            builder.and(posts.createdAt.goe(searchCondition.getStartDate()));
            builder.and(posts.createdAt.loe(searchCondition.getEndDate()));
        }

        return builder;
    }

    // 정렬 조건 동적 생성 (친구 게시물 항상 우선 + 정렬 조건별 분기)
    private OrderSpecifier<?>[] getOrderSpecifiers(PostSearchCondition searchCondition, Sort sort) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        // 1. 친구 게시물 우선 정렬
        if (searchCondition.getIsOnlyFriends() == null) {
            orders.add(
                    new CaseBuilder()
                            .when(friends.id.isNotNull()).then(0)
                            .otherwise(1)
                            .asc()
            );
        }

        // 2. 사용자가 요청한 정렬 조건 적용
        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;     // 정렬 방향

            switch (order.getProperty()) {
                case "comment" -> orders.add(new OrderSpecifier<>(direction, getCommentCountSubQuery()));   // 댓글 기준 정렬
                case "like" -> orders.add(new OrderSpecifier<>(direction, getLikesCountSubQuery()));        // 좋아요 기준 정렬
                case "createdAt" -> orders.add(new OrderSpecifier<>(direction, posts.createdAt));           // 생성일 기준 정렬
                case "modifiedAt" -> orders.add(new OrderSpecifier<>(direction, posts.modifiedAt));         // 수정일 기준 정렬
                default -> orders.add(posts.createdAt.desc());                                              // 기본값: 생성일 기준 내림차순
            }
        }

        // List -> 배열로 바꾸는 자바의 일반적인 패턴
        // new T[0]과 같은 빈 배열을 넘기면, 자바가 그 타입을 기준으로 적절한 크기의 배열을 만을어서 반환한다.
        return orders.toArray(new OrderSpecifier[0]);
    }

    // 친구 여부 서브쿼리 (로그인한 회원과 게시물 작성자가 친구(ACCEPT)인지 확인)
    private BooleanExpression getFriendCondition(Long userId) {

        return friends.status.eq(FriendsStatus.ACCEPT)
                .and(
                        friends.fromUser.id.eq(userId).and(friends.toUser.id.eq(posts.createdBy.id))
                                .or(friends.toUser.id.eq(userId).and(friends.fromUser.id.eq(posts.createdBy.id)))
                );
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

    // 좋아요 수 계산 조건
    private BooleanExpression likeCountCondition(QLikes likes, QPosts posts) {

        return likes.likeType.eq(LikeType.POST)
                .and(likes.likeTypeId.eq(posts.id));
    }

    // 로그인한 유저가 해당 게시글에 좋아요 눌렀는지 여부
    private BooleanExpression likedByMeCondition(QLikes likes, Long userId) {

        return likes.likeType.eq(LikeType.POST)
                .and(likes.likeTypeId.eq(posts.id))
                .and(likes.createdBy.id.eq(userId));
    }

}
