package dnd.dnd10_backend.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.board.dto.response
 * 클래스명 PostSimpleResponseDto
 * 클래스설명
 * 작성일 2023-03-04
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostSimpleResponseDto {
    private Long postId;

    private String title;

    private String content;
}
