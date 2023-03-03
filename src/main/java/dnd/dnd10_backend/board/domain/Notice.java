package dnd.dnd10_backend.board.domain;

import dnd.dnd10_backend.common.domain.BaseTimeEntity;
import dnd.dnd10_backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 패키지명 dnd.dnd10_backend.board.domain
 * 클래스명 Notice
 * 클래스설명
 * 작성일 2023-03-03
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-03-03] 엔티티 초안 작성 - 이우진
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;

    private String category;

    private String title;

    private boolean read;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    public void setRead() {
        this.read = true;
    }
}
