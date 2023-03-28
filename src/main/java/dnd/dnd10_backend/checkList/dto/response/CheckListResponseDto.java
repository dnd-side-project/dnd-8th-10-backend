package dnd.dnd10_backend.checkList.dto.response;

import dnd.dnd10_backend.checkList.domain.CheckList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 패키지명 dnd.dnd10_backend.checkList.dto.response
 * 클래스명 CheckListResponseDto
 * 클래스설명
 * 작성일 2023-02-05
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckListResponseDto {
    private LocalDate date;
    private Long checkIdx;
    private String content;
    private String status;

    public static CheckListResponseDto of(CheckList checkList){
        return new CheckListResponseDto(
                checkList.getCheckDate(),
                checkList.getCheckIdx(),
                checkList.getContent(),
                checkList.getStatus()
        );
    }
}
