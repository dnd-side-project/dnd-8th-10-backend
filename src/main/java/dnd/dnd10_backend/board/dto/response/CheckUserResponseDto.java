package dnd.dnd10_backend.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.board.dto.response
 * 클래스명 CheckUserResponseDto
 * 클래스설명
 * 작성일 2023-03-03
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-03-03] 체크 유저 리스트 응답 dto - 이우진
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CheckUserResponseDto {

    private int userCode;

    private String userName;

    private String email;
}
