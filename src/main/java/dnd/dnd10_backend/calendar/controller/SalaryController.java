package dnd.dnd10_backend.calendar.controller;

import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.calendar.dto.response.SalaryDetailResponseDto;
import dnd.dnd10_backend.calendar.dto.response.SalaryResponseDto;
import dnd.dnd10_backend.calendar.dto.response.StoreSalaryResponseDto;
import dnd.dnd10_backend.calendar.service.SalaryService;
import dnd.dnd10_backend.common.domain.SingleResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.service.ResponseService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.calendar.controller
 * 클래스명 SalaryController
 * 클래스설명
 * 작성일 2023-02-12
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-12] 직원페이지 급여 조회 기능 개발 - 이우진
 * [2023-02-13] 점장 급여 조회 기능, 상세 조회 기능 개발 - 이우진
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;
    private final UserService userService;

    //직원 페이지 급여 조회
    @GetMapping("/calendar/salary/worker")
    public ResponseEntity getWorkerSalary(HttpServletRequest request,
                                          @RequestParam String year,
                                          @RequestParam String month) {

        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        List<SalaryResponseDto> responseDto = salaryService.getWorkerSalary(year, month, user);

        SingleResponse<List<SalaryResponseDto>> response =
                ResponseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_SALARY);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/calendar/salary/manager")
    public ResponseEntity getManagerSalary(HttpServletRequest request,
                                           @RequestParam String year,
                                           @RequestParam String month) {

        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        Store store = user.getStore();

        List<StoreSalaryResponseDto> responseDto = salaryService.getStoreSalary(year, month, store);

        SingleResponse<List<StoreSalaryResponseDto>> response =
                ResponseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_SALARY);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/calendar/salary/manager/detail")
    public ResponseEntity getSalaryDetail(@RequestParam String year,
                                          @RequestParam String month,
                                          @RequestParam Long userCode) {

        User user = userService.findByUserCode(userCode);

        SalaryDetailResponseDto responseDto = salaryService.getSalaryDetail(year, month, user);

        SingleResponse<SalaryDetailResponseDto> response =
                ResponseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_SALARY);

        return ResponseEntity.ok().body(response);
    }
}
