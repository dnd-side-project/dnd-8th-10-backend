package dnd.dnd10_backend.board.dto.response;

import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 패키지명 dnd.dnd10_backend.board.dto.response
 * 클래스명 PostListResponseDto
 * 클래스설명
 * 작성일 2023-03-01
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-03-01] 카테고리별 게시글 리스트 응답 dto - 이우진
 */


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostListResponseDto {

    private Long postId;

    private String title;

    private String category;

    private int checkCount;

    private String userName;

    private Role role;

    private LocalDateTime createDate;

    private LocalDateTime modifiedDate;
}
