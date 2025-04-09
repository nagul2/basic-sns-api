package com.sns.api.friends.domain.entity;

import lombok.Getter;

/**
 * REFUSAL, CANCEL도 softdelete 해야 한다면 변경 가능
 */
@Getter
public enum FriendsStatus {
    ACCEPT,     // 친구 수락(등록 완료)
    PENDING,    // 친구 요청 대기 중
    REFUSAL,    // 친구 요청 거절 -> DB에서 삭제
    CANCEL,     // 친구 요청 취소 -> DB에서 삭제
}
