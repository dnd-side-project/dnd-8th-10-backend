package dnd.dnd10_backend.Inventory.dto.response;

import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.dto.response
 * 클래스명 InventoryRecordListResponseDto
 * 클래스설명
 * 작성일 2023-02-11
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-14] role 삭제 및 workDay 추가 - 원지윤
 * [2023-02-14] userProfileCode 추가 - 원지윤
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRecordListResponseDto {
    private String userName;
    private int userProfileCode;
    private String workDay;
    private String workTime;
    List<InventoryRecordResponseDto> list;

    public static InventoryRecordListResponseDto of(String userName, int userProfileCode, TimeCard timeCard, List<InventoryRecordResponseDto> list){
        return new InventoryRecordListResponseDto(
                userName,
                userProfileCode,
                timeCard.getMonth() + "월"+ timeCard.getDay()+"일",
                timeCard.getWorkTime(),
                list

        );
    }
}
