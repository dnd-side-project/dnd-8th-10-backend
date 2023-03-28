package dnd.dnd10_backend.store.controller;

import dnd.dnd10_backend.common.domain.SingleResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.service.ResponseService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.store.dto.response.StoreResponseDto;
import dnd.dnd10_backend.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 패키지명 dnd.dnd10_backend.store.controller
 * 클래스명 StoreController
 * 클래스설명
 * 작성일 2023-02-17
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {
    private final StoreService storeService;
    private final ResponseService responseService;

    /**
     *
     * @param request
     * @return
     */
    @GetMapping("/store")
    public ResponseEntity getStore(HttpServletRequest request){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        StoreResponseDto responseDto = storeService.getStoreInfo(token);

        SingleResponse<StoreResponseDto> response =
                responseService.getResponse(responseDto, CodeStatus.SUCCESS);

        return ResponseEntity.ok().body(response);
    }
}
