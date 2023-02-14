package dnd.dnd10_backend.Inventory.repository;

import dnd.dnd10_backend.Inventory.domain.InventoryUpdateRecord;
import dnd.dnd10_backend.Inventory.domain.enums.Category;
import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.repository
 * 클래스명 InventoryUpdateRecordRepository
 * 클래스설명
 * 작성일 2023-02-11
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
public interface InventoryUpdateRecordRepository extends JpaRepository<InventoryUpdateRecord, Long> {
    @Query("select i from InventoryUpdateRecord i where i.createTime <= :date")
    public List<InventoryUpdateRecord> findPastRecord(@Param("date")LocalDateTime endDateTime);

    public List<InventoryUpdateRecord> findByTimeCardAndCategory(TimeCard timeCard, Category category);

    public List<InventoryUpdateRecord> findByTimeCard(TimeCard timeCard);

    @Query("select i from InventoryUpdateRecord i where i.store = :store group by i.timeCard order by i.createTime desc")
    public List<InventoryUpdateRecord> findByStore(@Param("store")Store store);

    @Query("select i from InventoryUpdateRecord i where i.store = :store and i.category = :category group by i.timeCard order by i.createTime desc")
    public List<InventoryUpdateRecord> findByStoreAndCategory(@Param("store")Store store, @Param("category")Category category);
}
