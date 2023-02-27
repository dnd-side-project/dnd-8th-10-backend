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
    SUCCESS_SOCIAL_LOGIN(200, "소셜로그인 성공하였습니다."),
    SUCCESS_SEARCHED_USER(200, "사용자 조회에 성공하였습니다."),
    SUCCESS_TOKEN_REISSUED(200, "토큰 재발급에 성공하였습니다."),

    SUCCESS_SEARCHED_TIMECARD(200, "근무 내역 조회에 성공하였습니다."),
    SUCCESS_SEARCHED_WORKDAY(200, "근무 일자 조회에 성공하였습니다."),
    SUCCESS_SEARCHED_SALARY(200, "급여 조회에 성공하였습니다"),

    SUCCESS_UPDATED_USER(201, "사용자 정보 수정에 성공하였습니다."),
    SUCCESS_CREATED_USER(201, "사용자 등록에 성공하였습니다."),
    SUCCESS_DELETED_USER(200, "사용자 삭제에 성공하였습니다."),

    SUCCESS_SEARCHED_CHECKLIST(200, "체크리스트 조회에 성공하였습니다."),
    SUCCESS_UPDATED_CHECKLIST(200, "체크리스트 수정에 성공하였습니다."),
    SUCCESS_DELETED_CHECKLIST(200, "체크리스트 삭제에 성공하였습니다."),
    SUCCESS_CREATED_CHECKLIST(201, "체크리스트 등록에 성공하였습니다."),

    SUCCESS_SEARCHED_INVENTORY(200, "시재 조회에 성공하였습니다."),
    SUCCESS_CREATED_INVENTORY(201, "시재 등록에 성공하였습니다."),
    SUCCESS_UPDATED_INVENTORY(200, "시재 업데이트에 성공하였습니다."),
    SUCCESS_DELETED_INVENTORY(200, "시재 삭제에 성공하였습니다."),

    //40X
    ACCESS_TOKEN_EXPIRED(401,"엑세스 토큰이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED(401,"리프레쉬 토큰이 만료되었습니다."),
    INVALID_TOKEN(401,"유효하지 않은 토큰입니다."),
    UNAUTHORIZED(401,"인증되지 않은 사용자입니다."),
    NOT_FOUND_USER(404,"찾을 수 없는 사용자입니다."),
    NOT_FOUND_TIMECARD(404,"찾을 수 없는 근무이력입니다."),
    NOT_FOUND_CHECKLIST(404,"찾을 수 없는 체크리스트입니다."),
    NOT_FOUND_INVENTORY(404,"찾을 수 없는 시재입니다."),
    UNAUTHORIZED_DELETED_USER(401,"삭제 가능한 사용자가 아닙니다."),
    UNAUTHORIZED_UPDATED_USER(401,"업데이트 가능한 사용자가 아닙니다."),
    ALREADY_CREATED_INVENTORY(400, "이미 존재하는 담배입니다.");


    private final int code;
    private final String message;
}
