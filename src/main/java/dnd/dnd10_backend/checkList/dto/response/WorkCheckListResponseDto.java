package dnd.dnd10_backend.checkList.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkCheckListResponseDto {
    public boolean workDay;
    public List<CheckListResponseDto> list;

    public static WorkCheckListResponseDto of(boolean workDay, List<CheckListResponseDto> list){
        return new WorkCheckListResponseDto(
                workDay,
                list
        );
    }
}
