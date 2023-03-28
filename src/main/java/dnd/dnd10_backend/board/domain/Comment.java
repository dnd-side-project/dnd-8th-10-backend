package dnd.dnd10_backend.board.domain;

import com.sun.istack.NotNull;
import dnd.dnd10_backend.common.domain.BaseTimeEntity;
import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.board.domain
 * 클래스명 Comment
 * 클래스설명
 * 작성일 2023-02-28
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28] 댓글 엔티티 초안 작성 - 이우진
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @NotNull
    private String content;

    /*작성자 정보*/
    private Long userCode;

    private int userProfileCode;

    private String userName;

    private Role role;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public void update(String content) {
        this.content = content;
    }
}
