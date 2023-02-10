package dnd.dnd10_backend.store.repository;

import dnd.dnd10_backend.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * 패키지명 dnd.dnd10_backend.store.repository
 * 클래스명 StoreRepository
 * 클래스설명
 * 작성일 2023-02-09
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
public interface StoreRepository extends JpaRepository<Store, Long> {
    public Store findStoreByStoreName(String storeName);

    public Store findStoreByStoreIdx(Long StoreIdx);

}
