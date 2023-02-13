package dnd.dnd10_backend.calendar.dto.response;

import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.calendar.dto.response
 * 클래스명 SalaryDetailResponseDto
 * 클래스설명
 * 작성일 2023-02-13
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-13] 점장용 직원 급여 상세보기 응답 dto - 이우진
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SalaryDetailResponseDto {

    private int userProfileCode;

    private String userName;

    private Role role;

    private String workTime;

    private Double totalSalary;

    private List<SalaryResponseDto> daySalary;
}
