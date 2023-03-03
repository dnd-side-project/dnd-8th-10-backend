package dnd.dnd10_backend.board.controller;

import dnd.dnd10_backend.board.dto.request.CommentCreateDto;
import dnd.dnd10_backend.board.dto.request.CommentUpdateDto;
import dnd.dnd10_backend.board.dto.response.PostResponseDto;
import dnd.dnd10_backend.board.service.BoardService;
import dnd.dnd10_backend.board.service.CommentService;
import dnd.dnd10_backend.board.service.NoticeService;
import dnd.dnd10_backend.checkList.dto.response.CheckListResponseDto;
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

/**
 * 패키지명 dnd.dnd10_backend.board.controller
 * 클래스명 CommentController
 * 클래스설명
 * 작성일 2023-02-28
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28] 댓글 작성, 수정, 삭제 개발 - 이우진
 * [2023-03-03] 댓글 멘샨 시 알림 생성 - 이우진
 */


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final ResponseService responseService;
    private final BoardService boardService;
    private final NoticeService noticeService;

    //댓글 작성
    @PostMapping("/board/{postId}/comment")
    public ResponseEntity commentSave(@PathVariable Long postId,
                                      @RequestBody CommentCreateDto dto,
                                      HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        //멘션 한 경우 알림 생성
        if(!dto.getEmail().isEmpty()) {
            noticeService.createCommentNotice(user, dto.getEmail(), postId, dto.getContent());
        }

        commentService.save(dto, user, postId);

        PostResponseDto responseDto = boardService.get(postId, user);

        SingleResponse<PostResponseDto> response
                = responseService.getResponse(responseDto, CodeStatus.SUCCESS_CREATED_COMMENT);

        return ResponseEntity.ok().body(response);
    }

    //댓글 수정
    @PutMapping("board/{postId}/{commentId}")
    public ResponseEntity commentUpdate(HttpServletRequest request,
                                        @PathVariable Long postId,
                                        @PathVariable Long commentId,
                                        @RequestBody CommentUpdateDto dto) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        commentService.update(commentId, dto, user);

        PostResponseDto responseDto = boardService.get(postId, user);

        SingleResponse<PostResponseDto> response
                = responseService.getResponse(responseDto, CodeStatus.SUCCESS_UPDATED_COMMENT);

        return ResponseEntity.ok().body(response);
    }

    //댓글 삭제
    @DeleteMapping("/board/{postId}/{commentId}")
    public ResponseEntity commentDelete(HttpServletRequest request,
                                        @PathVariable Long postId,
                                        @PathVariable Long commentId) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        commentService.delete(commentId);

        PostResponseDto responseDto = boardService.get(postId, user);

        SingleResponse<PostResponseDto> response
                = responseService.getResponse(responseDto, CodeStatus.SUCCESS_DELETE_COMMENT);

        return ResponseEntity.ok().body(response);
    }

}
