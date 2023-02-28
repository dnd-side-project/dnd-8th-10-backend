package dnd.dnd10_backend.board.dto.response;

import dnd.dnd10_backend.board.domain.Comment;
import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponseDto {

    private Long commentId;

    private String content;

    private Long userCode;

    private String userName;

    private Role role;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.userCode = comment.getUserCode();
        this.userName = comment.getUserName();
        this.role = comment.getRole();
        this.createdDate = comment.getCreateDate();
        this.modifiedDate = comment.getModifiedDate();
    }
}
