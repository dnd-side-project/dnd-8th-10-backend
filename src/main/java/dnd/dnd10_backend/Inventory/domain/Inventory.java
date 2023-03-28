package dnd.dnd10_backend.Inventory.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dnd.dnd10_backend.Inventory.domain.enums.Category;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.domain
 * 클래스명 Inventory
 * 클래스설명
 * 작성일 2023-02-08
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_idx")
    private Long inventoryIdx;

    @Column(name = "inventory_name")
    private String inventoryName;

    @Column(name = "inventory_count")
    private int inventoryCount;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "store_idx")
    private Store store;

    @Builder
    public Inventory(String inventoryName, int inventoryCount,Category category, Store store){
        this.inventoryName = inventoryName;
        this.inventoryCount = inventoryCount;
        this.category = category;
        this.store = store;
    }

}
