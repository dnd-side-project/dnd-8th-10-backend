package dnd.dnd10_backend.Inventory.dto.response;

import dnd.dnd10_backend.Inventory.domain.InventoryUpdateRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.dto.response
 * 클래스명 InventoryRecordResponseDto
 * 클래스설명
 * 작성일 2023-02-11
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRecordResponseDto {
    private String inventoryName;
    private int diff;

    public static InventoryRecordResponseDto of(InventoryUpdateRecord updateRecord){
        return new InventoryRecordResponseDto(
                updateRecord.getInventoryName(),
                updateRecord.getDiff()
        );

    }
}
