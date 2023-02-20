package dnd.dnd10_backend.calendar.repository;

import dnd.dnd10_backend.calendar.domain.UserTimeCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTimeCardRepository extends JpaRepository<UserTimeCard, Long> {
    Optional<UserTimeCard> findByTimeCardId(Long timeCardId);
}
