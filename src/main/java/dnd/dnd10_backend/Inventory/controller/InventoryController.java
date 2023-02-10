package dnd.dnd10_backend.Inventory.controller;

import dnd.dnd10_backend.Inventory.domain.enums.Category;
import dnd.dnd10_backend.Inventory.dto.response.InventoryResponseDto;
import dnd.dnd10_backend.Inventory.service.InventoryService;
import dnd.dnd10_backend.common.domain.SingleResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.service.ResponseService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.controller
 * 클래스명 InventoryController
 * 클래스설명
 * 작성일 2023-02-10
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@RestController
@RequestMapping("/api")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ResponseService responseService;

    /**
     * 시재 조회 api
     * @param category
     * @param request
     * @return
     */
    @GetMapping("/inventory")
    public ResponseEntity getInventory(@RequestParam(value = "category", required = false)Category category,
                                       HttpServletRequest request){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");
        List<InventoryResponseDto> responseDtoList;
        if(category == null) {
            responseDtoList = inventoryService.findAll(token);
        }
        else {
            responseDtoList = inventoryService.findByCategory(category, token);
        }

        SingleResponse<List<InventoryResponseDto>> response = responseService.getResponse(responseDtoList,
                                                                          CodeStatus.SUCCESS_SEARCHED_INVENTORY);

        return ResponseEntity.ok().body(response);
    }

}
