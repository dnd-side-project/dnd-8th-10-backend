package dnd.dnd10_backend.checkList.repository;

import dnd.dnd10_backend.checkList.domain.CheckList;
import dnd.dnd10_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
 * [2023-02-13] 체크리스트 date, status로 체크리스트 찾도록 추가 - 원지윤
 * [2023-02-20] 체크리스트 조회 order by 추가 - 원지윤
 */
public interface CheckListRepository extends JpaRepository<CheckList, Long> {
    @Query(value = "select * from (select * from check_list c1 where c1.user_code = :user and c1.check_date = :date and c1.status = 'N' order by c1.check_idx DESC LIMIT 1000000) as A " + "UNION ALL "
            +"select * from ( select * from check_list c2 where c2.user_code = :user and c2.check_date = :date and c2.status = 'Y' order by c2.checked_time ASC LIMIT 1000000) as B", nativeQuery=true)
    List<CheckList> findCheckListByCheckDateAndUser(@Param("date")LocalDate date, @Param("user") User user);

    @Override
    @Query("select c from CheckList c join fetch c.user where c.checkIdx =:checkIdx")
    Optional<CheckList> findById(@Param(("checkIdx")) Long checkIdx);

    public List<CheckList> findCheckListByCheckDateAndAndStatusAndUser(LocalDate date, String status, User user);

    @Query("select c from CheckList c where c.checkDate <= :date")
    public List<CheckList> findCheckListByPastDate(@Param("date") LocalDate date);
}
