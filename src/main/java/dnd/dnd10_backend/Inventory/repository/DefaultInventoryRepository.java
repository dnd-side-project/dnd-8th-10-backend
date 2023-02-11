package dnd.dnd10_backend.Inventory.repository;

import dnd.dnd10_backend.Inventory.domain.DefaultInventory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.repository
 * 클래스명 DefaultInventoryRepository
 * 클래스설명
 * 작성일 2023-02-11
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
public interface DefaultInventoryRepository extends JpaRepository<DefaultInventory,Long> {
}
