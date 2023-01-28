package dnd.dnd10_backend.common.domain;

import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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