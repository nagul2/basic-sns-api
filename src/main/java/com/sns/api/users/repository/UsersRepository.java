package com.sns.api.users.repository;

import com.sns.api.users.domain.entity.Users;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    /**
     * 이메일을 기준으로 삭제되지 않은 회원 정보를 조회
     *
     * @param email 조회할 회원의 이메일
     * @return 해당 이메일을 가진 회원 정보
     */
    Optional<Users> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<Users> findByEmailIncludeDeleted(@Param("email") String email);

    /**
     * 회원 이름과 이메일을 기준으로 회원 목록을 검색하고 친구 id 목록에 포함된 회원을 우선 정렬하여 페이징
     * 검색 조건이 없을 경우 전체 조회
     *
     * @param pageable 페이징 정보를 담고 있는 Pageable 객체
     * @param username 검색할 회원 이름
     * @param email 검색할 이메일
     * @param friendIds 친구 id 목록
     * @return 조회된 회원 목록을 page 형태로 반환
     */
    @Query("""
            SELECT u FROM Users u 
            WHERE (:username IS NULL OR u.username LIKE %:username%) 
             AND (:email IS NULL OR u.email LIKE %:email%)
            ORDER BY CASE WHEN u.id IN :friendIds THEN 0 ELSE 1 END
            """)
    Page<Users> searchByUsernameAndEmail(Pageable pageable, @Param("username") String username,
                                         @Param("email") String email, @Param("friendIds") Set<Long> friendIds);
}
