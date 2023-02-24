package dnd.dnd10_backend.user.dto.response;

import dnd.dnd10_backend.store.domain.Store;
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
 * [2023-02-02] 사용자 휴대전화 번호 추가 - 원지윤
 * [2023-02-12] 사용자 프로필 인식 코드 추가 - 원지윤
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userCode;
    private String userName;
    private String kakaoEmail;
    private int userProfileCode;
    private Role role;
    private String phoneNumber;
    private String workTime;
    private String workPlace;
    private double wage;

    public static UserResponseDto of(User user, Store store){
        return new UserResponseDto(
                user.getUserCode(),
                user.getUsername(),
                user.getKakaoEmail(),
                user.getUserProfileCode(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getWorkTime(),
                store.getStoreName(),
                user.getWage()
        );
    }
}
