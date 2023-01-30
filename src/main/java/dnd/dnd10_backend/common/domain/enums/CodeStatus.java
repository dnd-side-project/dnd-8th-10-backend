package dnd.dnd10_backend.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 패키지명 dnd.dnd10_backend.common.domain.enums
 * 클래스명 CodeStatus
 * 클래스설명 응답 코드 및 메세지에 대한 enum
 * 작성일 2023-01-28
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@AllArgsConstructor
public enum CodeStatus {
    SUCCESS(200,"성공"),
    FAIL(-999,"실패"),
    //200
    SUCCESS_SOCIAL_LOGIN(200, "소셜로그인 성공");

    private final int code;
    private final String message;
}
