package dnd.dnd10_backend.Inventory.service;

import dnd.dnd10_backend.Inventory.domain.DefaultInventory;
import dnd.dnd10_backend.Inventory.domain.Inventory;
import dnd.dnd10_backend.Inventory.repository.DefaultInventoryRepository;
import dnd.dnd10_backend.Inventory.repository.InventoryRepository;
import dnd.dnd10_backend.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.service
 * 클래스명 DefaultInventoryService
 * 클래스설명
 * 작성일 2023-02-11
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-12] 기본 시재들의 default값 추가 - 원지윤
 */
@Service
@RequiredArgsConstructor
public class DefaultInventoryService {

    private final DefaultInventoryRepository defaultInventoryRepository;
    private final InventoryRepository inventoryRepository;

    /**
     * 기본 시재들을 저장하는 메소드
     * @param store 시재를 업데이트하려는 store 정보
     */
    public void saveDafaultInventories(Store store){
        List<DefaultInventory> list = defaultInventoryRepository.findAll();
        List<Inventory> inventoryList = list.stream()
                                    .map( t -> new Inventory(t.getInventoryName(),0,t.getCategory(),store))
                                    .collect(Collectors.toList());

        inventoryRepository.saveAll(inventoryList);
        return;
    }
}
