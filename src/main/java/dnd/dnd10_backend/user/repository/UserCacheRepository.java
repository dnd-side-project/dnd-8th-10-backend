package dnd.dnd10_backend.user.repository;

import dnd.dnd10_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
/**
 * 패키지명 dnd.dnd10_backend.user.repository
 * 클래스명 UserCacheRepository
 * 클래스설명 캐시 사용을 위한 repository 클래스
 * 작성일 2023-05-19
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserCacheRepository {
    private final UserRepository userRepository;

    @Cacheable(cacheNames="userCacheStore", key="#email")
    public User findByEmail(final String email) {
        log.info(">>> cache is not working");
        return userRepository.findByKakaoEmail(email);
    }
}
