package dnd.dnd10_backend.checkList.repository;

import dnd.dnd10_backend.checkList.domain.CheckList;
import dnd.dnd10_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 패키지명 dnd.dnd10_backend.checkList.repository
 * 클래스명 CheckListRepository
 * 클래스설명
 * 작성일 2023-02-05
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-08] 체크리스트 order by 추가 - 원지윤
 */
public interface CheckListRepository extends JpaRepository<CheckList, Long> {
    List<CheckList> findCheckListByDateAndUserOrderByStatusAsc(LocalDate date, User user);

    @Override
    Optional<CheckList> findById(Long checkIdx);
}
