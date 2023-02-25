package dnd.dnd10_backend.board.service;

import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.board.dto.request.PostCreateDto;
import dnd.dnd10_backend.board.repository.PostRepository;
import dnd.dnd10_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Post post = postRepository.findById(postId).orElseThrow();
        postRepository.delete(post);
    }
}
