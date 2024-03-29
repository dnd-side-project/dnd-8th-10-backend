package dnd.dnd10_backend.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.board.dto.request
 * 클래스명 PostUpdateDto
 * 클래스설명
 * 작성일 2023-03-01
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-03-01] 게시글 수정 dto - 이우진
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostUpdateDto {

    private String title;

    private String content;

    private String category;
}
