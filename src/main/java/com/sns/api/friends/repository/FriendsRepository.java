package com.sns.api.friends.repository;

import com.sns.api.friends.domain.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
}
