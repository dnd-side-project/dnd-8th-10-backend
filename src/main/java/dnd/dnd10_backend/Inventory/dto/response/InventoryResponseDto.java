package dnd.dnd10_backend.Inventory.dto.response;

import dnd.dnd10_backend.Inventory.domain.Inventory;
import dnd.dnd10_backend.Inventory.domain.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.dto.response
 * 클래스명 InventoryResponseDto
 * 클래스설명
 * 작성일 2023-02-10
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDto {
    private Long inventoryIdx;
    private String inventoryName;
    private Category category;

    public static InventoryResponseDto of(Inventory inventory){
        return new InventoryResponseDto(
                inventory.getInventoryIdx(),
                inventory.getInventoryName(),
                inventory.getCategory()
        );
    }
}
