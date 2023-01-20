package dnd.dnd10_backend.auth.repository;

import dnd.dnd10_backend.auth.domain.Token;
import dnd.dnd10_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 패키지명 dnd.dnd10_backend.auth.repository
 * 클래스명 TokenRepository
 * 클래스설명
 * 작성일 2023-01-19
 * 
 * @author 원지윤
 * @version 1.0
 * [수정내용] 
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */

public interface TokenRepository extends JpaRepository<Token, Long> {
    public Token findByUser(User user);
}
