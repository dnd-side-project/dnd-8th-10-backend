package dnd.dnd10_backend.board.controller;

import dnd.dnd10_backend.board.dto.request.PostCreateDto;
import dnd.dnd10_backend.board.dto.response.PostResponseDto;
import dnd.dnd10_backend.board.service.BoardService;
import dnd.dnd10_backend.calendar.dto.response.TimeCardResponseDto;
import dnd.dnd10_backend.common.domain.SingleResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.service.ResponseService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 패키지명 dnd.dnd10_backend.board.controller
 * 클래스명 BoardController
 * 클래스설명
 * 작성일 2023-02-28
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28] 게시글 작성, 삭제, 조회 기능 개발 - 이우진
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final ResponseService responseService;

    @PostMapping("/board/post")
    public void post(HttpServletRequest request,
                     @RequestBody PostCreateDto createDto) {

        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        boardService.write(createDto, user);
    }

    @DeleteMapping("/board/post/{postId}")
    public ResponseEntity delete(HttpServletRequest request,
                                 @PathVariable Long postId) {
        boardService.delete(postId);
        return ResponseEntity.ok(postId);
    }

    @GetMapping("/board/{postId}")
    public ResponseEntity get(@PathVariable Long postId,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        //조회수 중복 방지 로직
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }
        if(oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + postId.toString() + "]")) {
                this.boardService.updateView(postId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + postId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            this.boardService.updateView(postId);
            Cookie newCookie = new Cookie("postView", "[" + postId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }

        PostResponseDto responseDto = boardService.get(postId);
        SingleResponse<PostResponseDto> singleResponse =
                responseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_POST);

        return ResponseEntity.ok().body(singleResponse);
    }

    //게시글 수정

    //게시글 체크

    //게시판 홈화면 조회

    //게시글 검색
}
