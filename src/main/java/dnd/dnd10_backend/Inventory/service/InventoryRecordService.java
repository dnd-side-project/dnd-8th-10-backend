package dnd.dnd10_backend.Inventory.service;

import dnd.dnd10_backend.Inventory.repository.InventoryUpdateRecordRepository;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 */
@Service
public class InventoryRecordService {
    @Autowired
    private InventoryUpdateRecordRepository recordRepository;

    @Autowired
    private UserService userService;

    public void findInventoryUpdateRecords(final String token){
        User user = userService.getUserByEmail(token);
        Store store = user.getStore();

    }
}
