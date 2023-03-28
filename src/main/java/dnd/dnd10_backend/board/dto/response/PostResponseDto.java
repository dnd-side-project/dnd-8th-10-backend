package dnd.dnd10_backend.board.dto.response;

import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.board.dto.response
 * 클래스명 PostResponseDto
 * 클래스설명
 * 작성일 2023-02-28
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28글 게시 응답 dto - 이우진
 */

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

    private boolean check;
}
