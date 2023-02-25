package dnd.dnd10_backend.board.repository;

import dnd.dnd10_backend.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
