package dnd.dnd10_backend.user.dto.response;

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
@Setter
public class UserResponseDto {
    private String kakaoNickname;
    private String kakaoEmail;
    private String workTime;
    private String workPlace;
}
