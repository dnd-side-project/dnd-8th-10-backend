package dnd.dnd10_backend.user.repository;

import dnd.dnd10_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * 패키지명 dnd.dnd10_backend.user.repository
 * 클래스명 UserRepository
 * 클래스설명
 * 작성일 2023-01-18
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByKakaoEmail(String kakaoEmail);

    public User findByUserCode(Long userCode);
}
