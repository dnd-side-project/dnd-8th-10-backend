package dnd.dnd10_backend.board.service;

import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.board.dto.request.PostCreateDto;
import dnd.dnd10_backend.board.dto.response.CommentResponseDto;
import dnd.dnd10_backend.board.dto.response.PostResponseDto;
import dnd.dnd10_backend.board.repository.PostRepository;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * 패키지명 dnd.dnd10_backend.board.service
 * 클래스명 BoardService
 * 클래스설명
 * 작성일 2023-02-28
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28] 게시글 작성, 조회, 삭제 개발 - 이우진
 */

@Service
@RequiredArgsConstructor
public class BoardService {

    private final PostRepository postRepository;

    @Transactional
    public void write(PostCreateDto postCreateDto, User user) {
        postRepository.save(postCreateDto.toEntity(user));
    }

    @Transactional
    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));
        postRepository.delete(post);
    }

    @Transactional
    public void updateView(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));
        post.updateView(post.getViewCount());
        postRepository.save(post);
    }

    public PostResponseDto get(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_POST));

        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .checkCount(post.getCheckCount())
                .category(post.getCategory())
                .userCode(post.getUserCode())
                .userName(post.getUserName())
                .role(post.getRole())
                .createDate(post.getCreateDate())
                .modifiedDate(post.getModifiedDate())
                .comments(post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList()))
                .build();
    }
}
