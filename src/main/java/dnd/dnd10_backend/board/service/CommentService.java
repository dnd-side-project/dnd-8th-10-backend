package dnd.dnd10_backend.board.service;

import dnd.dnd10_backend.board.domain.Comment;
import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.board.dto.request.CommentCreateDto;
import dnd.dnd10_backend.board.dto.request.CommentUpdateDto;
import dnd.dnd10_backend.board.dto.response.CommentResponseDto;
import dnd.dnd10_backend.board.repository.CommentRepository;
import dnd.dnd10_backend.board.repository.PostRepository;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 패키지명 dnd.dnd10_backend.board.service
 * 클래스명 CommentService
 * 클래스설명
 * 작성일 2023-02-28
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28] 댓글 작성, 수정, 삭제 개발 - 이우진
 */

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //댓글 작성
    @Transactional
    public void save(CommentCreateDto dto, User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));

        Comment comment = dto.toEntity(user, post);
        commentRepository.save(comment);
    }

    //댓글 수정
    @Transactional
    public void update(Long commentId, CommentUpdateDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_COMMENT));
        comment.update(dto.getContent());
        commentRepository.save(comment);
    }

    //댓글 삭제
    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_COMMENT));
        commentRepository.delete(comment);
    }
}
