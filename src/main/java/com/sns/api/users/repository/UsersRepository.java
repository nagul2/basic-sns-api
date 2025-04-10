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
    Optional<Users> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<Users> findByEmailIncludeDeleted(@Param("email") String email);

    @Query("""
            SELECT u FROM Users u 
            WHERE (:username IS NULL OR u.username LIKE %:username%) 
             AND (:email IS NULL OR u.email LIKE %:email%)
            ORDER BY CASE WHEN u.id IN :friendIds THEN 0 ELSE 1 END
            """)
    Page<Users> searchByUsernameAndEmail(Pageable pageable, @Param("username") String username,
                                         @Param("email") String email, @Param("friendIds") Set<Long> friendIds);
}
