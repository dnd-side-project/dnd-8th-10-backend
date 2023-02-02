package dnd.dnd10_backend.user.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 패키지명 dnd.dnd10_backend.user.domain.enums
 * 클래스명 Role
 * 클래스설명 사용자의 역할을 구분하는 enum
 * 작성일 2023-01-28
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@AllArgsConstructor
public enum Role {
    WORKER("WORKER"),
    MANAGER("MANAGER");

    private final String value;

    @JsonCreator
    public static Role from(String sub) {
        for (Role role : Role.values()) {
            if (role.getValue().equals(sub)) {
                return role;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
