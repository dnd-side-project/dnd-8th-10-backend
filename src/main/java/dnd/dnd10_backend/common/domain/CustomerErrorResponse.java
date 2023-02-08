package dnd.dnd10_backend.common.domain;

import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 패키지명 dnd.dnd10_backend.common.domain
 * 클래스명 CustomerErrorResponse
 * 클래스설명 exceptionError에 대한 응답
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
public class CustomerErrorResponse extends CommonResponse{
    private int code;
    private String message;
    private long timeStamp;

    public CustomerErrorResponse(CodeStatus status, long timeStamp){
        this.code = status.getCode();
        this.message = status.getMessage();
        this.timeStamp = timeStamp;
    }

    public CustomerErrorResponse(int code, String message, long timeStamp){
        this.code = code;
        this.message = message;
        this.timeStamp = timeStamp;
    }
}