package dnd.dnd10_backend.calendar.domain;

import dnd.dnd10_backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 패키지명 dnd.dnd10_backend.calendar.domain
 * 클래스명 TimeCard
 * 클래스설명
 * 작성일 2023-02-06
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-06] TimeCard Entity 정의 - 이우진
 * [2023-02-08] TimeCard 수정 메서드 구현 - 이우진
 * [2023-02-11] workPlace storeName 으로 수정 - 이우진
 * [2023-02-20] user 와의 매핑 userTimeCard 와의 매핑으로 변경 - 이우진
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String year;

    private String month;

    private String day;

    private String workTime; // ”오전:7:00 - 오후12:00”

    private Double workHour; // 몇시간 근무 , ex 4.5, 5

    private String storeName; // 지점 정보

    private Long userCode;


    public void update(String workTime, Double workHour) {
        this.workTime = workTime;
        this.workHour = workHour;
    }

}
