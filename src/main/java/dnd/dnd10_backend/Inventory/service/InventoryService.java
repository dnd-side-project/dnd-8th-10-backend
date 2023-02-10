package dnd.dnd10_backend.Inventory.service;

import dnd.dnd10_backend.Inventory.domain.Inventory;
import dnd.dnd10_backend.Inventory.domain.enums.Category;
import dnd.dnd10_backend.Inventory.dto.response.InventoryResponseDto;
import dnd.dnd10_backend.Inventory.repository.InventoryRepository;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.store.repository.StoreRepository;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.service
 * 클래스명 InventoryService
 * 클래스설명
 * 작성일 2023-02-10
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Service
public class InventoryService {
    @Autowired
    private UserService userService;

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * inventory를 모두 조회하는 메소드
     * @param token access token
     * @return
     */
    public List<InventoryResponseDto> findAll(final String token){
        User user = userService.getUserByEmail(token);
        Store store = user.getStore();
        List<Inventory> inventoryList = inventoryRepository.findAllByStore(store);
        return convertInventoryToDto(inventoryList);
    }

    /**
     * category에 따라 inventory를 찾아주는 메소드
     * @param category 조회하려는 category
     * @param token access token
     * @return
     */
    public List<InventoryResponseDto> findByCategory(Category category, final String token){
        User user = userService.getUserByEmail(token);
        Store store = user.getStore();
        List<Inventory> inventoryList = inventoryRepository.findInventoryByCategoryAndStore(category, store);
        return convertInventoryToDto(inventoryList);
    }

    /**
     * List<Inventory>를  List<InventoryResponseDto> 변환시켜주는 메소드
     * @param inventoryList 변환시키려는 List<Inventory>
     * @return
     */
    public List<InventoryResponseDto> convertInventoryToDto(List<Inventory> inventoryList){
        List<InventoryResponseDto> responseList = new ArrayList<>();
        for(Inventory inventory: inventoryList){
            responseList.add(InventoryResponseDto.of(inventory));
        }
        return responseList;
    }
}
