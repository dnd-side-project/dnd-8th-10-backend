package dnd.dnd10_backend.user.dto.response;

import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 패키지명 dnd.dnd10_backend.user.dto.response
 * 클래스명 UserResponseDto
 * 클래스설명
 * 작성일 2023-01-28
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userCode;
    private String kakaoNickname;
    private String kakaoEmail;
    private Role role;
    private String workTime;
    private String workPlace;

    public static UserResponseDto of(User user){
        return new UserResponseDto(
                user.getUserCode(),
                user.getKakaoNickname(),
                user.getKakaoEmail(),
                user.getRole(),
                user.getWorkTime(),
                user.getWorkPlace()
        );
    }
}
