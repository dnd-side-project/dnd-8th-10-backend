package dnd.dnd10_backend.board.dto.response;

import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostResponseDto {

    private Long postId;

    private String title;

    private String content;

    private int viewCount;

    private int checkCount;

    private String category;

    private Long userCode;

    private String userName;

    private Role role;

    private LocalDateTime createDate;

    private LocalDateTime modifiedDate;

    private List<CommentResponseDto> comments;

}
