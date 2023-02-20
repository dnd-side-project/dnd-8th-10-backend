package dnd.dnd10_backend.calendar.domain;

import dnd.dnd10_backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 패키지명 dnd.dnd10_backend.calendar.domain
 * 클래스명 UserTimeCard
 * 클래스설명
 * 작성일 2023-02-20
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-20] UserTimeCard Entity 정의 - 이우진
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTimeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userCode;

    private String userName;

    private int userProfileCode;

    private Long timeCardId;

    private String workTime;
}
