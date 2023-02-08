package dnd.dnd10_backend.auth.domain;

import dnd.dnd10_backend.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * 패키지명 dnd.dnd10_backend.auth.domain
 * 클래스명 Token
 * 클래스설명 refresh token
 * 작성일 2023-01-19
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */

@Getter
@RequiredArgsConstructor
@Table(name = "token")
@Entity
public class Token {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_code")
    private User user;

    @Builder
    public Token(String refreshToken, User user) {
        this.refreshToken = refreshToken;
        this.user = user;
    }
    public void refreshUpdate(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
