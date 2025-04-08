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
    @JoinColumn(name = "from")
    private Users from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to")
    private Users to;

    @Enumerated(EnumType.STRING)
    private FriendsStatus status;

    public static Friends of(Users SendUser, Users ReceiveUser) {
        return Friends.builder()
                .from(SendUser)
                .to(ReceiveUser)
                .status(FriendsStatus.ACCEPT).build();
    }

}
