package dnd.dnd10_backend.calendar.controller;

import com.auth0.jwt.algorithms.Algorithm;
import dnd.dnd10_backend.calendar.dto.request.TimeCardCreateDto;
import dnd.dnd10_backend.calendar.service.CalendarService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
 */

@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    private final UserService userService;

    //근무시간 등록 API
    @PostMapping("/calendar")
    public void post(HttpServletRequest request,
                     @RequestBody TimeCardCreateDto requestDto) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        calendarService.save(requestDto, user);
    }

    //요일이랑 시간 리턴
    @GetMapping("/calendar")
    public ResponseEntity<String> get(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        return ResponseEntity.ok(user.getWorkTime());
    }
}
