package dnd.dnd10_backend.calendar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.calendar.dto.response
 * 클래스명 TimeCardResponseDto
 * 클래스설명
 * 작성일 2023-02-08
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-08] 응답 dto 구현 - 이우진
 * [2023-02-11] 유저 프로필 코드 추가 - 이우진
 */


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TimeCardResponseDto {

    private String name;

    private String workTime;

    private int userProfileCode;
}
