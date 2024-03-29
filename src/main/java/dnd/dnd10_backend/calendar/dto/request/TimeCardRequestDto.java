package dnd.dnd10_backend.calendar.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.calendar.dto.request
 * 클래스명 TimeCardRequestDto
 * 클래스설명
 * 작성일 2023-02-06
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-06] TimeCard request 요청 Dto 생성 - 이우진
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TimeCardRequestDto {

    private String year;

    private String month;

    private String day;
}
