package dnd.dnd10_backend.board.repository;

import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryAndStore(String category, Store store);
    List<Post> findByStore(Store store);
}
