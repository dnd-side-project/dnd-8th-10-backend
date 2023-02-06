package dnd.dnd10_backend.calendar.dto.request;

import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.calendar.dto.request
 * 클래스명 TimeCardCreateDto
 * 클래스설명
 * 작성일 2023-02-06
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-06] TimeCard 생성 Dto 생성 - 이우진
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeCardCreateDto {

    private String year;

    private String month;

    private String day;

    private String workTime;

    private Float workHour;

    public TimeCard toEntity(User user) {
        return TimeCard.builder()
                .year(year)
                .month(month)
                .day(day)
                .workTime(workTime)
                .workHour(workHour)
                .workPlace(user.getWorkPlace())
                .user(user)
                .build();
    }
}
