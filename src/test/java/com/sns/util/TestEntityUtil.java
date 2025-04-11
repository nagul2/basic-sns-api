package com.sns.util;

import java.lang.reflect.Field;

/**
 * Test에서만 사용하는 유틸 - 인스턴스 생성 방지하기 위해 abstract class로 작성
 */
public abstract class TestEntityUtil {

    /**
     * Entity ID 생성 전략이 IDENTITY(DB에서 자동 생성) 전략이므로 테스트에서 ID를 강제로 생성하기 위한 util 메서드
     */
    public static <T> void setId(T target, Long id) {
        try {
            Field idField = target.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(target, id);
        } catch (Exception e) {
            throw new RuntimeException("ID 필드 주입 실패");
        }
    }
}
