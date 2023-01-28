package dnd.dnd10_backend.common.exception;

import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import lombok.Getter;

/**
 * 패키지명 dnd.dnd10_backend.common.exception
 * 클래스명 CustomerNotFoundException
 * 클래스설명
 * 작성일 2023-01-28
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
public class CustomerNotFoundException extends RuntimeException{
    private CodeStatus status;
    public CustomerNotFoundException() {
        super();
    }

    public CustomerNotFoundException(CodeStatus status){
        super(status.getMessage());
        this.status = status;
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerNotFoundException(Throwable cause) {
        super(cause);
    }
}
