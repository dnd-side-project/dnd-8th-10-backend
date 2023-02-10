package dnd.dnd10_backend.store.domain;

import dnd.dnd10_backend.Inventory.domain.Inventory;
import dnd.dnd10_backend.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.store.domain
 * 클래스명 Store
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
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_idx")
    private Long storeIdx;

    @Column(name = "store_name")
    private String storeName;

    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    private List<Inventory> inventoryList = new ArrayList<>();

    @Builder
    public Store(String storeName){
        this.storeName = storeName;
    }
}
