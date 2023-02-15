package dnd.dnd10_backend.Inventory.service;

import dnd.dnd10_backend.Inventory.domain.InventoryUpdateRecord;
import dnd.dnd10_backend.Inventory.domain.enums.Category;
import dnd.dnd10_backend.Inventory.dto.response.InventoryRecordListResponseDto;
import dnd.dnd10_backend.Inventory.dto.response.InventoryRecordResponseDto;
import dnd.dnd10_backend.Inventory.repository.InventoryUpdateRecordRepository;
import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.calendar.repository.TimeCardRepository;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.service
 * 클래스명 InventoryRecordService
 * 클래스설명
 * 작성일 2023-02-11
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-12] 시재 기록 삭제하는 스케쥴러 추가 - 원지윤
 * [2023-02-12] 시재 기록 조회하는 메소드 추가 - 원지윤
 * [2023-02-14] findInventoryByInventoryName -> findInventoryByStoreAndInventoryName 수정 - 원지윤
 * [2023-02-14] 카테고리별 시재기록 조회 안되는 오류 수정 - 원지윤
 */
@Service
@RequiredArgsConstructor
public class InventoryRecordService {

    private final InventoryUpdateRecordRepository recordRepository;
    private final UserService userService;

    /**
     * 시재 기록 조회하는 메소드
     * @param token access token
     */
    public List<InventoryRecordListResponseDto> findInventoryUpdateRecords(Category category, final String token){
        User user = userService.getUserByEmail(token);
        Store store = user.getStore();
        List<InventoryUpdateRecord> list;
        //category가 null이면 전체를 조회
        if(category == null){
            list = recordRepository.findByStore(store);
            return findAllInventoryUpdateRecords(list);
        }

        list = recordRepository.findByStoreAndCategory(store, category);

        return findInventoryUpdateRecordsByCategory(list, category);

    }

    /**
     * 모든 카테고리의 시재 기록을 조회하는 메소드
     * @param list
     * @return
     */
    public List<InventoryRecordListResponseDto> findAllInventoryUpdateRecords(List<InventoryUpdateRecord> list){
        List<InventoryRecordListResponseDto> responseDtoList = new ArrayList<>();

        for(InventoryUpdateRecord i : list){
            List<InventoryUpdateRecord> recordList = recordRepository.findByTimeCard(i.getTimeCard());
            responseDtoList.add(InventoryRecordListResponseDto.of(i.getUser(),i.getTimeCard(),convertToInventoryRecordToDto(recordList)));
        }
        return responseDtoList;
    }

    /**
     * 카테고리별 시재 기록을 조회하는 메소드
     * @param list
     * @param category
     * @return
     */
    public List<InventoryRecordListResponseDto> findInventoryUpdateRecordsByCategory(List<InventoryUpdateRecord> list, Category category){
        List<InventoryRecordListResponseDto> responseDtoList = new ArrayList<>();

        for(InventoryUpdateRecord i : list){
            List<InventoryUpdateRecord> recordList = recordRepository.findByTimeCardAndCategory(i.getTimeCard(), category);
            responseDtoList.add(InventoryRecordListResponseDto.of(i.getUser(),i.getTimeCard(),convertToInventoryRecordToDto(recordList)));
        }
        return responseDtoList;
    }

    /**
     * List<InventoryUpdateRecord>를 List<InventoryRecordResponseDto>로 변환시켜주는 메소드
     * @param recordList 변환시키려는 list
     * @return
     */
    public List<InventoryRecordResponseDto> convertToInventoryRecordToDto(List<InventoryUpdateRecord> recordList){
        List<InventoryRecordResponseDto> responseDtoList = new ArrayList<>();
        for(InventoryUpdateRecord ir: recordList){
            responseDtoList.add(InventoryRecordResponseDto.of(ir));
        }
        return responseDtoList;
    }


    /**
     * 매일 실행되면서 60일 지난 InventoryUpdateRecord 데이터를 찾아 삭제해주는 메소드
     */
    @Scheduled(cron = "0 0 5 * * ?") //매일 오전 5시마다 실행
    public void deletePastRecord(){
        //60일 지난 데이터들을 삭제
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul")); // 현재시간
        LocalDateTime endDateTime = now.minusDays(60); //60일 전 날짜 계산
        List<InventoryUpdateRecord> list = recordRepository.findPastRecord(endDateTime);

        for(InventoryUpdateRecord i: list){
            recordRepository.delete(i);
        }

        return;
    }
}
