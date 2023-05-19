package dnd.dnd10_backend.board.controller;

import dnd.dnd10_backend.board.dto.response.NoticeResponseDto;
import dnd.dnd10_backend.board.service.NoticeService;
import dnd.dnd10_backend.common.domain.SingleResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.service.ResponseService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.board.controller
 * 클래스명 NoticeController
 * 클래스설명
 * 작성일 2023-03-03
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28] 알림 controller 개발 - 이우진
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final UserService userService;

    //새로운 알림 있는지 확인
    @GetMapping("notice")
    public boolean checkNotice(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        return noticeService.check(user);
    }

    //알림 창에서 알림 확인
    @GetMapping("notice/list")
    public ResponseEntity getNotice(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        List<NoticeResponseDto> responseDto = noticeService.getNotice(user);
        noticeService.read(user); //알림 읽음 처리
        SingleResponse<List<NoticeResponseDto>> singleResponse =
                ResponseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_NOTICE);

        return ResponseEntity.ok().body(singleResponse);
    }
}
