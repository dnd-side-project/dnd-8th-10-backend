package dnd.dnd10_backend.board.domain;

import dnd.dnd10_backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 패키지명 dnd.dnd10_backend.board.domain
 * 클래스명 PostCheck
 * 클래스설명
 * 작성일 2023-03-01
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-21] 게시글 체크 엔티티 개발 - 이우진
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user; //좋아요 한 멤버

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; //좋아요 한 게시글
}
