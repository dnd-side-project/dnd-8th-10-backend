package dnd.dnd10_backend.checkList.repository;

import dnd.dnd10_backend.checkList.domain.DefaultCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * 패키지명 dnd.dnd10_backend.checkList.repository
 * 클래스명 DefaultCheckListRepository
 * 클래스설명 기본 체크리스트를 위한 repository
 * 작성일 2023-02-15
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
public interface DefaultCheckListRepository extends JpaRepository<DefaultCheckList, Long> {

}
