package dnd.dnd10_backend.checkList.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 패키지명 dnd.dnd10_backend.checkList.dto.request
 * 클래스명 CheckListRequestDto
 * 클래스설명
 * 작성일 2023-02-05
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckListRequestDto {
    private LocalDate date;
    private String content;
    private String status;
}
