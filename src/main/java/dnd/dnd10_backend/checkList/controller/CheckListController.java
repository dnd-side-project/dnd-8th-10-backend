package dnd.dnd10_backend.checkList.controller;

import dnd.dnd10_backend.checkList.dto.request.CheckListRequestDto;
import dnd.dnd10_backend.checkList.dto.request.UpdateCheckListRequestDto;
import dnd.dnd10_backend.checkList.dto.response.CheckListResponseDto;
import dnd.dnd10_backend.checkList.dto.response.WorkCheckListResponseDto;
import dnd.dnd10_backend.checkList.service.CheckListService;
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
 * 패키지명 dnd.dnd10_backend.checkList.controller
 * 클래스명 CheckListController
 * 클래스설명
 * 작성일 2023-02-05
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-08] 체크리스트 조회를 dto -> String으로 변경 - 원지윤
 */
@RestController
@RequestMapping("/api")
public class CheckListController {

    @Autowired
    private CheckListService checkListService;

    @Autowired
    private ResponseService responseService;

    /**
     * 체크리스트를 저장하는 api
     * @param requestDto 저장하려는 체크리스트 dto
     * @param
     * @return
     */
    @PostMapping("/checkList")
    public ResponseEntity createCheckList(@RequestBody CheckListRequestDto requestDto,
                                          HttpServletRequest request){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        List<CheckListResponseDto> responseDto
                = checkListService.saveCheckList(requestDto, token);

        SingleResponse<List<CheckListResponseDto>> response
                = responseService.getResponse(responseDto,
                                CodeStatus.SUCCESS_CREATED_CHECKLIST);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 체크리스트 조회하는 api
     * @param date 조회하려는 날짜
     * @param request
     * @return
     */
    @GetMapping("/checkList")
    public ResponseEntity showCheckList(@RequestParam("date") String date,
                                        HttpServletRequest request){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        WorkCheckListResponseDto responseDto = checkListService.findCheckList(date,token);

        SingleResponse<WorkCheckListResponseDto> response
                = responseService.getResponse(responseDto,CodeStatus.SUCCESS_SEARCHED_CHECKLIST);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 체크리스트를 업데이트하는 api
     * @param requestDto 업데이트 하려는 체크리스트 dto
     * @param request
     * @return
     */
    @PutMapping("/checkList")
    public ResponseEntity updateCheckList(@RequestBody UpdateCheckListRequestDto requestDto,
                                          HttpServletRequest request){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        List<CheckListResponseDto> responseDto = checkListService.updateCheckList(requestDto, token);

        SingleResponse<List<CheckListResponseDto>> response
                = responseService.getResponse(responseDto, CodeStatus.SUCCESS_UPDATED_CHECKLIST);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 체크리스트 삭제 api
     * @param checkIdx 삭제하려는 체크리스트 idx
     * @param request
     * @return
     */
    @DeleteMapping("/checkList")
    public ResponseEntity deleteCheckList(@RequestParam("check")Long checkIdx,
                                          HttpServletRequest request){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");
        List<CheckListResponseDto> responseDto = checkListService.deleteCheckList(checkIdx, token);

        SingleResponse<List<CheckListResponseDto>> response
                = responseService.getResponse(responseDto, CodeStatus.SUCCESS_DELETED_CHECKLIST);

        return ResponseEntity.ok().body(response);
    }
}
