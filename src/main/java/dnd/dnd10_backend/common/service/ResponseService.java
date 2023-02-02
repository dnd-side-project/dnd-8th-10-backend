package dnd.dnd10_backend.common.service;

import dnd.dnd10_backend.common.domain.CommonResponse;
import dnd.dnd10_backend.common.domain.SingleResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.domain.enums.ResponseStatus;
import org.springframework.stereotype.Service;

/**
 * 패키지명 dnd.dnd10_backend.common.service
 * 클래스명 ResponseService
 * 클래스설명 응답 함수를 관리하는 service 클래스
 * 작성일 2023-01-28
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Service
public class ResponseService {

    /**
     * 데이터 response 성공시 사용하는 함수
     * @param data response해줄 data
     * @param status 응답 코드 및 메세지를 담은 enum
     * @param <T>
     * @return
     */
    public <T> SingleResponse<T> getResponse(T data, CodeStatus status){
        return new SingleResponse(ResponseStatus.SUCCESS, status,data);
    }
}
