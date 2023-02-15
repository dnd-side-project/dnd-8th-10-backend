package dnd.dnd10_backend.checkList.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 패키지명 dnd.dnd10_backend.checkList.domain
 * 클래스명 DefaultCheckList
 * 클래스설명 기본 체크리스트를 위한 엔티티 클래스
 * 작성일 2023-02-15
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "default_check_list")
public class DefaultCheckList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_idx")
    private Long checkIdx;

    @Column(name = "content")
    private String content;
}
