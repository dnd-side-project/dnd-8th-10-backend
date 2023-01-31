package dnd.dnd10_backend.user.controller;

import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.oauth.domain.OauthToken;
import dnd.dnd10_backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.user.controller
 * 클래스명 UserController
 * 클래스설명
 * 작성일 2023-01-18
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * 프론트에 토큰 관련 헤더 노출 - 원지윤
 */
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = {"Authorization","Refresh"})
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    // 프론트에서 인가코드 돌려 받는 주소
    // 인가 코드로 엑세스 토큰 발급 -> 사용자 정보 조회 -> DB 저장 -> jwt 토큰 발급 -> 프론트에 토큰 전달
    @GetMapping("/oauth/token")
    public ResponseEntity getLogin(@RequestParam("code") String code) { //(1)

        // 넘어온 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = userService.getAccessToken(code);

        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        List<String> tokenList = userService.saveUserAndGetToken(oauthToken.getAccess_token());

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.AT_HEADER_STRING, JwtProperties.TOKEN_PREFIX + tokenList.get(0));

        headers.add(JwtProperties.RT_HEADER_STRING, JwtProperties.TOKEN_PREFIX + tokenList.get(1));

        return ResponseEntity.ok().headers(headers).body("success");
    }

    @GetMapping("/oauth/token/refresh")
    public ResponseEntity refresh(HttpServletRequest request){

        List<String> tokenList = userService.reissueRefreshToken(request.getHeader(JwtProperties.RT_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX,""));

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.AT_HEADER_STRING, JwtProperties.TOKEN_PREFIX + tokenList.get(0));

        headers.add(JwtProperties.RT_HEADER_STRING, JwtProperties.TOKEN_PREFIX + tokenList.get(1));

        return ResponseEntity.ok().headers(headers).body("success");
    }

}
