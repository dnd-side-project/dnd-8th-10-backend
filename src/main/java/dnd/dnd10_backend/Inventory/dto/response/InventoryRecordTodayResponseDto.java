package dnd.dnd10_backend.Inventory.dto.response;

import dnd.dnd10_backend.Inventory.domain.InventoryUpdateRecord;
import dnd.dnd10_backend.calendar.domain.TimeCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.dto.response
 * 클래스명 InventoryRecordTodayResponseDto
 * 클래스설명
 * 작성일 2023-03-02
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRecordTodayResponseDto {
    private String userName;
    private int userProfileCode;
    private String workTime;
    private String inventorySummumation;

    public static InventoryRecordTodayResponseDto of(InventoryUpdateRecord record, String inventorySummumation){
        return new InventoryRecordTodayResponseDto(
                record.getUserName(),
                record.getUserProfileCode(),
                record.getTimeCard().getWorkTime(),
                inventorySummumation

        );
    }
}
