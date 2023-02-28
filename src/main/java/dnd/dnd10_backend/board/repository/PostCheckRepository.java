package dnd.dnd10_backend.board.repository;

import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.board.domain.PostCheck;
import dnd.dnd10_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCheckRepository extends JpaRepository<PostCheck,  Long> {
    boolean existsByPostAndUser(Post post, User user);

    void deleteByPostAndUser(Post post, User user);
}
