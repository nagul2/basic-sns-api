package com.sns.api.friends.repository;

import com.sns.api.friends.domain.entity.Friends;
import com.sns.api.users.domain.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
    boolean existsByFromUserAndToUser(Users findSendUser, Users findReceiveUser);

    /**
     * 로그인 유저 Id를 기준으로 친구 데이터를 조회하는 쿼리
     *
     * 연관된 Entity인 fromUser, toUser를 한번에 가져오기 위한 @EntityGraph 사용(Page와 fetch join을 함께 사용하면 메모리에서 페이징 되기 때문에 위험함)
     * where 조건: 접속한 유저를 기준으로 fromUser, toUser에 본인이 있을 수 있기 때문에 or 조건 지정
     * and: 친구인 상태(ACCEPT)만 조회
     * order by: 조회된 친구의 username 필드를 기준으로 오름차순 정렬하기 위한 case 문
     *
     * @param loginUserId 로그인 유저 Id
     * @param pageable 페이징 정보
     * @return 조회된 친구 데이터 반환(페이징 적용)
     */
    @EntityGraph(attributePaths = {"fromUser", "toUser"})
    @Query("""
        select f from Friends f
        where (f.fromUser.id = :loginUserId or f.toUser.id = :loginUserId)
            and f.status = 'ACCEPT'
        order by case
            when f.fromUser.id = :loginUserId then f.toUser.username
            else f.fromUser.username
            end 
    """)
    Page<Friends> findAcceptedFriendsByLoginUserId(@Param("loginUserId") Long loginUserId, Pageable pageable);
}
