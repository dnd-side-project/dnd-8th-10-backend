package dnd.dnd10_backend.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import dnd.dnd10_backend.config.enums.CacheType;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 패키지명 dnd.dnd10_backend.config
 * 클래스명 CachingConfig
 * 클래스설명 캐시 관련 설정파일
 * 작성일 2023-05-19
 * 
 * @author 원지윤
 * @version 1.0
 * [수정내용] 
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Configuration
@EnableCaching
public class CachingConfig {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
                .map(cache -> new CaffeineCache(
                        cache.getName(),
                        Caffeine.newBuilder()
                                .expireAfterWrite(cache.getExpireAfterWrite(), TimeUnit.SECONDS)
                                .maximumSize(cache.getMaximumSize())
                                .recordStats()
                                .build()
                ))
                .collect(Collectors.toList());

        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
