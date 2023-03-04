package dnd.dnd10_backend.board.repository;

import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryAndStore(String category, Store store);

    List<Post> findByStore(Store store);

    List<Post> findByUserCode(Long userCode);

    List<Post> findByTitleContainingOrContentContainingOrUserNameContainingAndStore(String keyword1, String keyword2, String keyword3, Store store);

    default List<Post> search(String keyword, Store store) {
        return findByTitleContainingOrContentContainingOrUserNameContainingAndStore(keyword, keyword, keyword, store);
    }
}
