package dnd.dnd10_backend.Inventory.dto.request;

import dnd.dnd10_backend.Inventory.domain.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.dto.request
 * 클래스명 UpdateInventoryListRequestDto
 * 클래스설명
 * 작성일 2023-02-11
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
public class UpdateInventoryListRequestDto {
    private Category category;
    private List<UpdateInventoryRequestDto> list;
}
