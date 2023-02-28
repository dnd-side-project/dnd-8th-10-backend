package dnd.dnd10_backend.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.board.dto.response
 * 클래스명 CheckResponseDto
 * 클래스설명
 * 작성일 2023-03-01
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-21] 게시글 체크 응답 dto 개발 - 이우진
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CheckResponseDto {

    private int checkCount;

    private boolean status;
}
