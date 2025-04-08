package com.sns.api.friends.repository;

import com.sns.api.friends.domain.entity.Friends;
import com.sns.api.users.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
    boolean existsByFromUserAndToUser(Users findSendUser, Users findReceiveUser);
}
