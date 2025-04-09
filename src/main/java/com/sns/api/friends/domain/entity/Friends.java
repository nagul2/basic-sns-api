package com.sns.api.friends.domain.entity;

import com.sns.api.common.domain.entity.BaseTimeEntity;
import com.sns.api.users.domain.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 실무에서는 직접 테이블을 만들 때 index를 적용하지만 local DB를 사용하고 실행 될 때마다 테이블을 다시 생성할 것이므로 index 설정을 추가
@Table(name = "friends", indexes = {
        @Index(name = "idx_from_user", columnList = "from_user_id"),
        @Index(name = "idx_to_user", columnList = "to_user_id"),
        @Index(name = "idx_status", columnList = "status")
})
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
