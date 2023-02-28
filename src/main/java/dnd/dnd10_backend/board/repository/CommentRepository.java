package dnd.dnd10_backend.board.repository;

import dnd.dnd10_backend.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
