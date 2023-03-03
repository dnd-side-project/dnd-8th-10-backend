package dnd.dnd10_backend.board.repository;

import dnd.dnd10_backend.board.domain.Notice;
import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByUser(User user);

    boolean existsByUserAndRead(User user, boolean read);
}
