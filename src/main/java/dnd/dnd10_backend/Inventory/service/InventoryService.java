package dnd.dnd10_backend.Inventory.service;

import dnd.dnd10_backend.Inventory.domain.DefaultInventory;
import dnd.dnd10_backend.Inventory.domain.Inventory;
import dnd.dnd10_backend.Inventory.domain.InventoryUpdateRecord;
import dnd.dnd10_backend.Inventory.domain.enums.Category;
import dnd.dnd10_backend.Inventory.dto.request.CreateInventoryRequestDto;
import dnd.dnd10_backend.Inventory.dto.request.UpdateInventoryListRequestDto;
import dnd.dnd10_backend.Inventory.dto.request.UpdateInventoryRequestDto;
import dnd.dnd10_backend.Inventory.dto.response.InventoryResponseDto;
import dnd.dnd10_backend.Inventory.repository.DefaultInventoryRepository;
import dnd.dnd10_backend.Inventory.repository.InventoryRepository;
import dnd.dnd10_backend.Inventory.repository.InventoryUpdateRecordRepository;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
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

    @Autowired
    private InventoryUpdateRecordRepository inventoryUpdateRecordRepository;

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
     * 새로운 담배 저장 메소드
     * @param requestDto 저장하려는 담배 정보를 담은 dto
     * @param token access token
     * @return
     */
    public List<InventoryResponseDto> saveInventory(CreateInventoryRequestDto requestDto, final String token){
        User user = userService.getUserByEmail(token);
        Store store = user.getStore();
        Inventory inventory = inventoryRepository.findInventoryByInventoryName(requestDto.getInventoryName());

        if(inventory != null) throw new CustomerNotFoundException(CodeStatus.ALREADY_CREATED_INVENTORY);

        inventory = Inventory.builder()
                .inventoryName(requestDto.getInventoryName())
                .category(Category.CIGARETTE)
                .store(store)
                .build();

        inventoryRepository.save(inventory);
        
        List<Inventory> inventoryList = inventoryRepository.findInventoryByCategoryAndStore(Category.CIGARETTE, store);
        return convertInventoryToDto(inventoryList);
    }

    /**
     * 시재 업데이트 메소드
     * @param listRequestDto
     * @param token
     * @return
     */
    public List<InventoryResponseDto> updateInventory(UpdateInventoryListRequestDto listRequestDto, final String token){
        User user = userService.getUserByEmail(token);
        Store store = user.getStore();
        Category category = listRequestDto.getCategory();

        List<UpdateInventoryRequestDto> list = listRequestDto.getList();

        for(UpdateInventoryRequestDto i: list){
            Inventory inventory = inventoryRepository.findInventoryByInventoryName(i.getInventoryName());

            InventoryUpdateRecord record = InventoryUpdateRecord.builder()
                    .inventory(inventory)
                    .diff(i.getDiff())
                    .category(category)
                    .user(user)
                    .store(store)
                    .build();

            inventoryUpdateRecordRepository.save(record);
            
        }

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
