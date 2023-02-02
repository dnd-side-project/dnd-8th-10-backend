package dnd.dnd10_backend.user.dto.request;

import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveRequestDto {
    private String kakaoEmail;
    private Role role;
    private String workPlace;
    private String workTime;
}
