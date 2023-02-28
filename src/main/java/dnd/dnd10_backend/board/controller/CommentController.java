package dnd.dnd10_backend.board.controller;

import dnd.dnd10_backend.board.dto.request.CommentCreateDto;
import dnd.dnd10_backend.board.dto.request.CommentUpdateDto;
import dnd.dnd10_backend.board.dto.response.PostResponseDto;
import dnd.dnd10_backend.board.service.BoardService;
import dnd.dnd10_backend.board.service.CommentService;
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
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final ResponseService responseService;
    private final BoardService boardService;

    //댓글 작성
    @PostMapping("/board/{postId}/comment")
    public ResponseEntity commentSave(@PathVariable Long postId,
                                      @RequestBody CommentCreateDto dto,
                                      HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        User user = userService.getUserByEmail(token);

        commentService.save(dto, user, postId);

        PostResponseDto responseDto = boardService.get(postId);

        SingleResponse<PostResponseDto> response
                = responseService.getResponse(responseDto, CodeStatus.SUCCESS_CREATED_COMMENT);

        return ResponseEntity.ok().body(response);
    }

    //댓글 수정
    @PutMapping("board/{postId}/{commentId}")
    public ResponseEntity commentUpdate(@PathVariable Long postId,
                                        @PathVariable Long commentId,
                                        @RequestBody CommentUpdateDto dto) {
        commentService.update(commentId, dto);

        PostResponseDto responseDto = boardService.get(postId);

        SingleResponse<PostResponseDto> response
                = responseService.getResponse(responseDto, CodeStatus.SUCCESS_UPDATED_COMMENT);

        return ResponseEntity.ok().body(response);
    }

    //댓글 삭제
    @DeleteMapping("/board/{postId}/{commentId}")
    public ResponseEntity commentDelete(@PathVariable Long postId,
                                        @PathVariable Long commentId) {
        commentService.delete(commentId);

        PostResponseDto responseDto = boardService.get(postId);

        SingleResponse<PostResponseDto> response
                = responseService.getResponse(responseDto, CodeStatus.SUCCESS_DELETE_COMMENT);

        return ResponseEntity.ok().body(response);
    }

}
