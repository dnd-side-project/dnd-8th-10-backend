package dnd.dnd10_backend.Inventory.domain;

import dnd.dnd10_backend.Inventory.domain.enums.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.domain
 * 클래스명 DefaultInventory
 * 클래스설명 편의점에 기본적으로 제공해줄 시재 entity
 * 작성일 2023-02-11
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "default_inventory")
public class DefaultInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_idx")
    private Long inventoryIdx;

    @Column(name = "inventory_name")
    private String inventoryName;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;
}
