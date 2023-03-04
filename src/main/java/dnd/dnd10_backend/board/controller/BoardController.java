package dnd.dnd10_backend.board.controller;

import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.board.dto.request.PostCreateDto;
import dnd.dnd10_backend.board.dto.request.PostUpdateDto;
import dnd.dnd10_backend.board.dto.response.*;
import dnd.dnd10_backend.board.service.BoardService;
import dnd.dnd10_backend.board.service.ImageService;
import dnd.dnd10_backend.board.service.NoticeService;
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
import java.util.List;

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
 * [2023-03-01] 게시글 체크, 수정 기능 개발 - 이우진
 * [2023-03-01] 카테고리 별 게시글 리스트 조회 기능 개발 - 이우진
 * [2023-03-02] 게시글 검색 기능 개발 - 이우진
 * [2023-03-03] 게시글 작성 시 알림 생성, 체크 유저 리스트 반환 api 개발 - 이우진
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final ResponseService responseService;
    private final NoticeService noticeService;
    private final ImageService imageService;

    @PostMapping("/board")
    public ResponseEntity post(HttpServletRequest request,
                     @RequestBody PostCreateDto createDto) {

        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        Post post = boardService.write(createDto, user);

        noticeService.createNotice(post, user);

        PostSimpleResponseDto responseDto = PostSimpleResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        SingleResponse<PostSimpleResponseDto> response = responseService.getResponse(responseDto, CodeStatus.SUCCESS);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/board/{postId}")
    public ResponseEntity delete(@PathVariable Long postId) {
        boardService.delete(postId);
        imageService.deleteImageByPostId(postId);
        return ResponseEntity.ok(postId);
    }

    @GetMapping("/board/{postId}")
    public ResponseEntity get(@PathVariable Long postId,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        //조회수 중복 방지 로직
        //response.setHeader("Set-Cookie", "Test1=TestCookieValue1; Secure; SameSite=None");
        viewCountUp(postId, request, response);

        PostResponseDto responseDto = boardService.get(postId, user);
        SingleResponse<PostResponseDto> singleResponse =
                responseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_POST);

        return ResponseEntity.ok().body(singleResponse);
    }

    //게시글 수정
    @PutMapping("board/{postId}")
    public ResponseEntity update(HttpServletRequest request,
                                 @PathVariable Long postId,
                                 @RequestBody PostUpdateDto dto) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        boardService.update(postId, dto, user);

        PostResponseDto responseDto = boardService.get(postId, user);
        SingleResponse<PostResponseDto> singleResponse =
                responseService.getResponse(responseDto, CodeStatus.SUCCESS_UPDATED_POST);

        return ResponseEntity.ok().body(singleResponse);
    }

    //게시글 체크
    @PostMapping("board/{postId}/check")
    public ResponseEntity check(HttpServletRequest request,
                                @PathVariable Long postId) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        CheckResponseDto responseDto = boardService.checkPost(postId, user);
        SingleResponse<CheckResponseDto> singleResponse =
                responseService.getResponse(responseDto, CodeStatus.SUCCESS_UPDATED_POSTCHECK);

        return ResponseEntity.ok().body(singleResponse);
    }

    //게시판 홈화면 조회
    @GetMapping("board/category")
    public ResponseEntity postList(HttpServletRequest request,
                                   @RequestParam String category) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        List<PostListResponseDto> responseDto = boardService.getPostList(category, user);
        SingleResponse<List<PostListResponseDto>> singleResponse =
                responseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_POST);

        return ResponseEntity.ok().body(singleResponse);
    }

    //게시글 검색
    @GetMapping("board/search")
    public ResponseEntity postSearch(HttpServletRequest request,
                                     @RequestParam String keyword) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        List<PostListResponseDto> responseDto = boardService.postSearch(keyword, user);
        SingleResponse<List<PostListResponseDto>> singleResponse =
                responseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_POST);

        return ResponseEntity.ok().body(singleResponse);
    }

    //체크한 사람 목록
    @GetMapping("board/{postId}/check")
    public ResponseEntity getCheckUserList(@PathVariable Long postId) {

        List<CheckUserResponseDto> responseDto = boardService.getCheckUserList(postId);
        SingleResponse<List<CheckUserResponseDto>> singleResponse =
                responseService.getResponse(responseDto, CodeStatus.SUCCESS_UPDATED_POSTCHECK);

        return ResponseEntity.ok().body(singleResponse);
    }

    @GetMapping("myPost")
    public ResponseEntity getMyPost(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        List<PostListResponseDto> responseDto = boardService.getMyPost(user);
        SingleResponse<List<PostListResponseDto>> singleResponse =
                responseService.getResponse(responseDto, CodeStatus.SUCCESS_SEARCHED_POST);

        return ResponseEntity.ok().body(singleResponse);
    }

    //조회수 방지 로직
    private void viewCountUp(Long id, HttpServletRequest req, HttpServletResponse res) {

        Cookie oldCookie = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("boardView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + id.toString() + "]")) {
                boardService.updateView(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                res.addCookie(oldCookie);
            }
        } else {
            boardService.updateView(id);
            Cookie newCookie = new Cookie("boardView","[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            res.addCookie(newCookie);
        }
    }
}
