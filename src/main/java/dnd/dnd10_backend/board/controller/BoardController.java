package dnd.dnd10_backend.board.controller;

import dnd.dnd10_backend.board.dto.request.PostCreateDto;
import dnd.dnd10_backend.board.service.BoardService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

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
}
