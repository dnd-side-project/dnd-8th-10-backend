package dnd.dnd10_backend.common.domain;

import dnd.dnd10_backend.common.domain.enums.ResponseStatus;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 패키지명 dnd.dnd10_backend.common.domain
 * 클래스명 CommonResponse
 * 클래스설명 공통 response 속성을 제공하는 도메인
 * 작성일 2023-01-28
 * 
 * @author 원지윤
 * @version 1.0
 * [수정내용] 
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {
    ResponseStatus status;
    int code;
    String message;

    public CommonResponse(ResponseStatus status, CodeStatus codeStatus){
        this.status = status;
        this.code = codeStatus.getCode();
        this.message = codeStatus.getMessage();
    }
}
