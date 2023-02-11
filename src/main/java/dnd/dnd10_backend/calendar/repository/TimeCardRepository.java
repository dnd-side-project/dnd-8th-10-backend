package dnd.dnd10_backend.calendar.repository;

import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

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
 * [2023-02-08] (날짜, 지점) 검색 메서드, (날짜, 유저) 검색 메서드 추가 - 이우진
 * [2023-02-08] workPlace storeName으로 수정 - 이우진
 */

public interface TimeCardRepository extends JpaRepository<TimeCard, Long> {
    List<TimeCard> findByYearAndMonthAndDayAndStoreName(String year, String month, String day, String storeName);

    List<TimeCard> findByYearAndMonthAndUser(String year, String month, User user);

    Optional<TimeCard> findByYearAndMonthAndDayAndUser(String year, String month, String day, User user);
}
