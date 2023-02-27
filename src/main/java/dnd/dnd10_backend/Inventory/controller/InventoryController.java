package dnd.dnd10_backend.Inventory.controller;

import dnd.dnd10_backend.Inventory.domain.enums.Category;
import dnd.dnd10_backend.Inventory.dto.request.CreateInventoryRequestDto;
import dnd.dnd10_backend.Inventory.dto.request.UpdateInventoryListRequestDto;
import dnd.dnd10_backend.Inventory.dto.response.InventoryRecordListResponseDto;
import dnd.dnd10_backend.Inventory.dto.response.InventoryResponseDto;
import dnd.dnd10_backend.Inventory.service.InventoryRecordService;
import dnd.dnd10_backend.Inventory.service.InventoryService;
import dnd.dnd10_backend.common.domain.SingleResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.service.ResponseService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
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
 * [2023-02-12] 시재 기록 카테고리 별로 조회 가능하도록 변경 - 원지윤
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryRecordService recordService;
    private final ResponseService responseService;

    /**
     * 시재 조회 api
     * @param category 조회하려는 카테고리
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

    /**
     * 시재 저장 api (담배만 새로 추가 가능)
     * @param requestDto
     * @param request
     * @return
     */
    @PostMapping("/inventory")
    public ResponseEntity createInventory(@RequestBody CreateInventoryRequestDto requestDto,
                                                  HttpServletRequest request){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        List<InventoryResponseDto> responseDtoList = inventoryService.saveInventory(requestDto, token);

        SingleResponse<List<InventoryResponseDto>> response = responseService.getResponse(responseDtoList,
                CodeStatus.SUCCESS_CREATED_INVENTORY);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 시재 업데이트 api
     * @param requestDto
     * @param request
     * @return
     */
    @PutMapping("/inventory")
    public ResponseEntity updateInventory(@RequestBody UpdateInventoryListRequestDto requestDto,
                                                HttpServletRequest request){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        List<InventoryResponseDto> responseDtoList = inventoryService.updateInventory(requestDto, token);

        SingleResponse<List<InventoryResponseDto>> response = responseService.getResponse(responseDtoList,
                CodeStatus.SUCCESS_UPDATED_INVENTORY);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 시재 업데이트 기록 조회 api
     * @param category 시재의 종류
     * @param request
     * @return
     */
    @GetMapping("/inventory/record")
    public ResponseEntity getInventoryRecord(@RequestParam(value = "category", required = false)Category category,
                                              HttpServletRequest request){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");
        List<InventoryRecordListResponseDto> responseDtoList = recordService.findInventoryUpdateRecords(category, token);
        SingleResponse<List<InventoryRecordListResponseDto>> response = responseService.getResponse(responseDtoList,
                CodeStatus.SUCCESS_SEARCHED_INVENTORY);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 시재 삭제 api
     * @param inventoryIdx 삭제하려는 시재의 idx
     * @param request
     * @return
     */
    @DeleteMapping("/inventory")
    public ResponseEntity deleteInventory(@RequestParam Long inventoryIdx,
                                          HttpServletRequest request){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");
        inventoryService.deleteInventory(inventoryIdx, token);

        SingleResponse<String> response = responseService.getResponse("",CodeStatus.SUCCESS_DELETED_INVENTORY);

        return ResponseEntity.ok().body(response);
    }

}
