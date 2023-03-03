package dnd.dnd10_backend.board.dto.request;

import dnd.dnd10_backend.board.domain.Comment;
import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.board.dto.request
 * 클래스명 CommentCreateDto
 * 클래스설명
 * 작성일 2023-02-28
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28] 댓글 작성 dto - 이우진
 * [2023-03-03] 댓글 멘션 시 이메일 값 추가 - 이우진
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentRequestDto {

    private String content;
    private List<Long> userCode;

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .content(content)
                .userCode(user.getUserCode())
                .userProfileCode(user.getUserProfileCode())
                .userName(user.getUsername())
                .role(user.getRole())
                .post(post)
                .build();
    }
}
