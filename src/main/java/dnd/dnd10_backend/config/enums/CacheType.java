package dnd.dnd10_backend.config.enums;

import lombok.Getter;
/**
 * 패키지명 dnd.dnd10_backend.config.enums
 * 클래스명 CacheType
 * 클래스설명 여러 캐시를 저장하기 위한 캐시 타입
 * 작성일 2023-05-19
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
public enum CacheType {

    USERCACHESTORE("userCacheStore");

    private String name;
    private int expireAfterWrite;
    private int maximumSize;

    CacheType(String name) {
        this.name = name;
        this.expireAfterWrite = ConstConfig.DEFAULT_TTL_SEC;
        this.maximumSize = ConstConfig.DEFAULT_MAX_SIZE;
    }

    static class ConstConfig {
        static final int DEFAULT_TTL_SEC = 3000;
        static final int DEFAULT_MAX_SIZE = 10000;
    }

}