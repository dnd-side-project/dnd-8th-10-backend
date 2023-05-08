package dnd.dnd10_backend.user.dto.request;

import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.Getter;

/**
 * 패키지명 dnd.dnd10_backend.user.dto.request
 * 클래스명 UserRequestDto
 * 클래스설명
 * 작성일 2023-01-28
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
public class UserRequestDto {
    private Role role;
    private String workPlace;
    private String workTime;
}
