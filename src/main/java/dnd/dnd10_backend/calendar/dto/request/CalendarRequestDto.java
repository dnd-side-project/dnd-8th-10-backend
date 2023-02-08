package dnd.dnd10_backend.calendar.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CalendarRequestDto {

    private String year;

    private String month;
}
