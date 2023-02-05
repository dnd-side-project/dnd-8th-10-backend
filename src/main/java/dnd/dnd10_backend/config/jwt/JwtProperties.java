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
    Long AT_EXP_TIME =  60000L * 30; //30분
    Long RT_EXP_TIME = 60000L * 60 * 24 * 14; //14일 (2주)
    String TOKEN_PREFIX = "Bearer ";
    String AT_HEADER_STRING = "Authorization";
    String RT_HEADER_STRING = "Refresh";
}
