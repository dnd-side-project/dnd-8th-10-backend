package dnd.dnd10_backend.user.dto.request;

import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 패키지명 dnd.dnd10_backend.user.dto.request
 * 클래스명 UserSaveRequestDto
 * 클래스설명 사용자 저장을 위한 request dto
 * 작성일 2023-02-02
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveRequestDto {
    private String kakaoEmail;
    private Role role;
    private String phoneNumber;
    private String workPlace;
    private String workTime;
}
