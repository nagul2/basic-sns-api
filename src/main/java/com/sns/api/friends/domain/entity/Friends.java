package com.sns.api.friends.domain.entity;

import com.sns.api.common.domain.entity.BaseTimeEntity;
import com.sns.api.users.domain.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "frineds")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friends extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private Users fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private Users toUser;

    @Enumerated(EnumType.STRING)
    private FriendsStatus status;

    public void accept() {
        this.status = FriendsStatus.ACCEPT;
    }

    public static Friends of(Users SendUser, Users ReceiveUser) {
        return Friends.builder()
                .fromUser(SendUser)
                .toUser(ReceiveUser)
                .status(FriendsStatus.PENDING).build();
    }

}
