package dnd.dnd10_backend.Inventory.repository;

import dnd.dnd10_backend.Inventory.domain.Inventory;
import dnd.dnd10_backend.Inventory.domain.enums.Category;
import dnd.dnd10_backend.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.repository
 * 클래스명 InventoryRepository
 * 클래스설명
 * 작성일 2023-02-10
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-14] 시재 조회 시 매장 조건 추가 - 원지윤
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("select i from Inventory i where i.store = :store order by i.category")
    public List<Inventory> findAllByStore(@Param("store")Store store);

    public List<Inventory> findInventoryByCategoryAndStore(Category category, Store store);

    public Inventory findInventoryByStoreAndInventoryName(Store store, String inventoryName);

}
