package dnd.dnd10_backend.calendar.controller;

import com.auth0.jwt.algorithms.Algorithm;
import dnd.dnd10_backend.calendar.dto.request.TimeCardCreateDto;
import dnd.dnd10_backend.calendar.dto.request.UpdateTimeCardRequestDto;
import dnd.dnd10_backend.calendar.dto.response.TimeCardResponseDto;
import dnd.dnd10_backend.calendar.service.CalendarService;
import dnd.dnd10_backend.common.domain.SingleResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.service.ResponseService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.auth0.jwt.JWT.require;

/**
 * 패키지명 dnd.dnd10_backend.calendar.controller
 * 클래스명 CalendarController
 * 클래스설명
 * 작성일 2023-02-06
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-06] TimeCard 저장 기능 구현 - 이우진
 * [2023-02-08] TimeCard 수정, 삭제, 조회 기능 구현 - 이우진
 * [2023-02-10] 근무시간 subString 수정 - 이우진
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    private final UserService userService;
    private final ResponseService responseService;

    //근무시간 등록 API
    @PostMapping("/calendar")
    public void createTimeCard(HttpServletRequest request,
                     @RequestBody TimeCardCreateDto requestDto) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        calendarService.saveTimeCard(requestDto, user);
    }

//    //요일이랑 시간 리턴
//    @GetMapping("/calendar")
//    public ResponseEntity<String> get(HttpServletRequest request) {
//        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
//                .replace(JwtProperties.TOKEN_PREFIX,"");
//
//        User user = userService.getUserByEmail(token);
//
//        return ResponseEntity.ok(user.getWorkTime());
//    }

    //수정 API
    @PutMapping("/calendar")
    public void updateTimeCard(HttpServletRequest request,
                               @RequestBody UpdateTimeCardRequestDto requestDto) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        calendarService.updateTimeCard(requestDto, user);
    }

    //삭제 요청 API
    @DeleteMapping("/calendar")
    public void deleteTimeCard(HttpServletRequest request,
                               @RequestParam String year,
                               @RequestParam String month,
                               @RequestParam String day) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        calendarService.deleteTimeCard(year, month, day, user);
    }

    //오늘 날짜를 누른 경우
    @GetMapping("/calendar/{day}")
    public ResponseEntity<String> getWorkTime(@PathVariable String day,
                                      HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);
        String workTime = user.getWorkTime();
        String workDay = day;

        switch (day) {
            case "mon": workDay = "월";
                break;
            case "tue": workDay = "화";
                break;
            case "wed": workDay = "수";
                break;
            case "thu": workDay = "목";
                break;
            case "fri": workDay = "금";
                break;
            case "sat": workDay = "토";
                break;
            case "sun": workDay = "일";
                break;
        }

        int index = workTime.indexOf(workDay);
        String time;
        if(index == -1) {
            time = "";
        } else {
            time = workDay.substring(index+2, index+13);
        }

        return ResponseEntity.ok().body(time); //인덱스 값 맞는지 테스트 필요
    }

    //과거의 날짜를 눌렀을때 같은 지점 출근 내역 조회
    @GetMapping("calendar/detail")
    public ResponseEntity getTimeCards(HttpServletRequest request,
                                       @RequestParam String year,
                                       @RequestParam String month,
                                       @RequestParam String day) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        List<TimeCardResponseDto> responseDto = calendarService.getTimeCards(year, month, day, user.getWorkPlace());

        SingleResponse<List<TimeCardResponseDto>> response
                = responseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_TIMECARD);

        return ResponseEntity.ok().body(response);
    }

    //해당 달의 '본인' 출근한 날짜
    @GetMapping("calendar")
    public ResponseEntity getWorkDay(HttpServletRequest request,
                                     @RequestParam String year,
                                     @RequestParam String month) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        List<String> responseDto = calendarService.getWorkDay(year, month, user);

        SingleResponse<List<String>> response
                = responseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_WORKDAY);

        return ResponseEntity.ok().body(response);
    }
}
