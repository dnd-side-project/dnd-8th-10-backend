package dnd.dnd10_backend.Inventory.service;

import dnd.dnd10_backend.Inventory.domain.DefaultInventory;
import dnd.dnd10_backend.Inventory.domain.Inventory;
import dnd.dnd10_backend.Inventory.repository.DefaultInventoryRepository;
import dnd.dnd10_backend.Inventory.repository.InventoryRepository;
import dnd.dnd10_backend.store.domain.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class DefaultInventoryService {
    @Autowired
    private DefaultInventoryRepository defaultInventoryRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * 기본 시재들을 저장하는 메소드
     * @param store
     */
    public void saveDafaultInventories(Store store){
        List<DefaultInventory> list = defaultInventoryRepository.findAll();

        for(DefaultInventory i: list){
            Inventory inventory = Inventory.builder()
                    .inventoryName(i.getInventoryName())
                    .inventoryCount(0)
                    .category(i.getCategory())
                    .store(store)
                    .build();

            inventoryRepository.save(inventory);
        }
        return;
    }
}
