package dnd.dnd10_backend.common.service;

import dnd.dnd10_backend.common.domain.CustomerErrorResponse;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 패키지명 dnd.dnd10_backend.common.service
 * 클래스명 CustomerRestExceptionHandler
 * 클래스설명
 * 작성일 2023-01-28
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Slf4j
@RestControllerAdvice("dnd.dnd10_backend")
public class CustomerRestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<CustomerErrorResponse> handleException(CustomerNotFoundException exc){
        CustomerErrorResponse error = new CustomerErrorResponse(
                exc.getStatus(),
                System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CustomerErrorResponse> handleException(Exception exc){
        CustomerErrorResponse error = new CustomerErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
