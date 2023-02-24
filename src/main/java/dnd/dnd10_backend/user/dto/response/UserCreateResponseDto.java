package dnd.dnd10_backend.user.dto.response;

import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.user.dto.response
 * 클래스명 UserCreateResponseDto
 * 클래스설명
 * 작성일 2023-02-24
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateResponseDto {
    private Long userCode;
    private String userName;
    private String kakaoEmail;
    private int userProfileCode;
    private Role role;


    public static UserCreateResponseDto of(User user){
        return new UserCreateResponseDto(
                user.getUserCode(),
                user.getUsername(),
                user.getKakaoEmail(),
                user.getUserProfileCode(),
                user.getRole()
        );
    }
}
