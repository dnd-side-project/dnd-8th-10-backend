package dnd.dnd10_backend.user.dto.response;

import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 패키지명 dnd.dnd10_backend.user.dto.response
 * 클래스명 UserStoreResponseDto
 * 클래스설명
 * 작성일 2023-02-17
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-03-02] 이메일 필드 추가 - 원지윤
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStoreResponseDto {
    private int userProfileCode;
    private String userName;
    private Role role;
    private String email;
    private String phoneNumber;
    private String workTime;
    private double wage;

    public static UserStoreResponseDto of(User user){
        return new UserStoreResponseDto(
                user.getUserProfileCode(),
                user.getUsername(),
                user.getRole(),
                user.getKakaoEmail(),
                user.getPhoneNumber(),
                user.getWorkTime(),
                user.getWage()
        );
    }

}
