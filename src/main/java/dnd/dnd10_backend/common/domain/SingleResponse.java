package dnd.dnd10_backend.common.domain;

import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.domain.enums.ResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 패키지명 dnd.dnd10_backend.common.domain
 * 클래스명 SingleResponse
 * 클래스설명
 * 작성일 2023-01-28
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@Setter
@NoArgsConstructor
public class SingleResponse<T> extends CommonResponse {
    T data;

    public SingleResponse(ResponseStatus status,  CodeStatus codeStatus, T data){
        this.data = data;
        this.status = status;
        this.code = codeStatus.getCode();
        this.message = codeStatus.getMessage();
    }
}