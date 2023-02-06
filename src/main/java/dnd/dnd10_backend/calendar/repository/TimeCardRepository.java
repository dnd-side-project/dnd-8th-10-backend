package dnd.dnd10_backend.calendar.repository;

import dnd.dnd10_backend.calendar.domain.TimeCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 패키지명 dnd.dnd10_backend.calendar.repository
 * 클래스명 TimeCardRepository
 * 클래스설명
 * 작성일 2023-02-06
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-06] TimeCard JPA Repository 생성 - 이우진
 */

public interface TimeCardRepository extends JpaRepository<TimeCard, Long> {
}
