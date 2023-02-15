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
import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.calendar.repository.TimeCardRepository;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
 * [2023-02-12] 재고 차이 저장하도록 수정 - 원지윤
 * [2023-02-12] 시재 근무 시간 내에 여러번 작성 시 엔티티 수정되도록 변경 - 원지윤
 * [2023-02-13] exception발생 시 500에러 안뜨도록 codestatus 사용 - 원지윤
 */
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final UserService userService;
    private final InventoryRepository inventoryRepository;
    private final InventoryUpdateRecordRepository inventoryUpdateRecordRepository;
    private final TimeCardRepository timeCardRepository;

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
        Inventory inventory = inventoryRepository
                .findInventoryByStoreAndInventoryName(store, requestDto.getInventoryName());

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
        TimeCard timeCard = findTimeCard(user, store);

        List<InventoryUpdateRecord> recordList = inventoryUpdateRecordRepository.findByTimeCard(timeCard);

        for(UpdateInventoryRequestDto i: list){
            Inventory inventory = inventoryRepository
                    .findInventoryByStoreAndInventoryName(store, i.getInventoryName());
            inventory.setInventoryCount(i.getDiff());
            inventoryRepository.save(inventory);

            InventoryUpdateRecord record = new InventoryUpdateRecord();

            for(InventoryUpdateRecord rc: recordList){
                if(!rc.getInventoryName().equals(i.getInventoryName())){
                    continue;
                }
                rc.setDiff(i.getDiff());
                record = rc;
            }

            if(record.getInventoryName()==null){
                record = InventoryUpdateRecord.builder()
                        .inventory(inventory)
                        .timeCard(timeCard)
                        .diff(i.getDiff())
                        .category(category)
                        .user(user)
                        .store(store)
                        .build();
            }

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

    public TimeCard findTimeCard(User user, Store store){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul")); // 현재시간
        List<TimeCard> list = timeCardRepository.findByUserAndStoreName(user, store.getStoreName());
        if(list == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_TIMECARD);

        for(TimeCard t: list){
            String[] time = t.getWorkTime().split("~");

            String month = t.getMonth().length()<2 ? "0"+t.getMonth() : t.getMonth();
            String day = t.getDay().length()<2 ? "0"+t.getDay() : t.getDay();
            String[] HM1 = time[0].split(":");
            String[] HM2 = time[1].split(":");
            LocalDateTime startTime = LocalDateTime.parse(t.getYear() +"-"+month+"-"+day+" "+HM1[0]+":"+HM1[1]+":00", formatter);
            LocalDateTime endTime = LocalDateTime.parse(t.getYear() +"-"+month+"-"+day+" "+HM2[0]+":"+HM2[1]+":00", formatter);
            if((startTime.isBefore(now) && endTime.isAfter(now)) || startTime.isEqual(now) || endTime.isEqual(now)){
                return t;
            }

        }
        throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_TIMECARD);
    }
}
