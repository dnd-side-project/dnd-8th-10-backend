package dnd.dnd10_backend.common.domain;

import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.domain.enums.ResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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