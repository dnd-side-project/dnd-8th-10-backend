package dnd.dnd10_backend.board.dto.request;

import dnd.dnd10_backend.board.domain.Comment;
import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentCreateDto {

    private String content;

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .content(content)
                .userCode(user.getUserCode())
                .userName(user.getUsername())
                .role(user.getRole())
                .post(post)
                .build();
    }
}
