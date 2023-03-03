package dnd.dnd10_backend.board.dto.response;

import dnd.dnd10_backend.board.domain.Comment;
import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 패키지명 dnd.dnd10_backend.board.dto.response
 * 클래스명 CommentResponseDto
 * 클래스설명
 * 작성일 2023-02-28
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28] 댓글 응답 dto - 이우진
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponseDto {

    private Long commentId;

    private String content;

    private int userProfileCode;

    private Long userCode;

    private String userName;

    private Role role;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.userProfileCode = comment.getUserProfileCode();
        this.userCode = comment.getUserCode();
        this.userName = comment.getUserName();
        this.role = comment.getRole();
        this.createdDate = comment.getCreateDate();
        this.modifiedDate = comment.getModifiedDate();
    }
}
