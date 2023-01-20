package dnd.dnd10_backend.config.jwt;

/**
 * 패키지명 dnd.dnd10_backend.config.jwt
 * 클래스명 JwtProperties
 * 클래스설명
 * 작성일 2023-01-19
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2022-01-21] refresh token에 대한 property 추가 - 원지윤
 */
public interface JwtProperties {
    String SECRET = "DND810";
    Long AT_EXP_TIME =  30 * 60 * 1000L; //30분 //60000 1분 //864000000 10일
    Long RT_EXP_TIME = 60 * 60 * 24 * 7 * 1000L; //1주일
    String TOKEN_PREFIX = "Bearer ";
    String AT_HEADER_STRING = "Authorization";
    String RT_HEADER_STRING = "Refresh";
}
