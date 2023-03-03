package dnd.dnd10_backend.board.repository;

import dnd.dnd10_backend.board.domain.Image;
import dnd.dnd10_backend.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPost(Post post);

    Optional<Image> findAllByPostAndOriginalFileName(Post postm, String originalFileName);
}
