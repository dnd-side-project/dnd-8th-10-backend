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
 */
public interface JwtProperties {
    String SECRET = "DND810";
    int EXPIRATION_TIME =  864000000; //60000 1분 //864000000 10일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
