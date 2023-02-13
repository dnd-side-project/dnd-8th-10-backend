package dnd.dnd10_backend.calendar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.calendar.dto.response
 * 클래스명 SalaryResponseDto
 * 클래스설명
 * 작성일 2023-02-12
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-12]  알바생 급여 조회 응답 dto - 이우진
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SalaryResponseDto {

    private String month;

    private String day;

    private String workTime;

    private Double workHour;

    private Double salary;
}
