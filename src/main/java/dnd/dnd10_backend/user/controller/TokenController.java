package dnd.dnd10_backend.user.controller;

import dnd.dnd10_backend.common.domain.SingleResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.service.ResponseService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.dto.response.UserCreateResponseDto;
import dnd.dnd10_backend.user.dto.response.UserResponseDto;
import dnd.dnd10_backend.user.oauth.domain.OauthToken;
import dnd.dnd10_backend.user.service.TokenService;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.user.controller
 * 클래스명 TokenController
 * 클래스설명 토큰 관련 컨트롤러
 * 작성일 2023-02-02
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-06] getUserByToekn -> getUserByToken 오타수정 - 이우진
 * [2022-02-07] 프론트 local 판단 param 추가 - 원지윤
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class TokenController {

    private final UserService userService;
    private final TokenService tokenService;
    private final ResponseService responseService;

    // 프론트에서 인가코드 돌려 받는 주소
    // 인가 코드로 엑세스 토큰 발급 -> 사용자 정보 조회 -> DB 저장 -> jwt 토큰 발급 -> 프론트에 토큰 전달
    @GetMapping("/token")
    public ResponseEntity getLogin(@RequestParam("code") String code,
                                   @RequestParam("isLocal") boolean isLocal,
                                   HttpServletRequest request,
                                   HttpServletResponse res) { //(1)

        // 넘어온 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = tokenService.getAccessToken(code, isLocal);

        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        List<String> tokenList = tokenService.saveUserAndGetToken(oauthToken.getAccess_token());

        //발급 받은 jwtToken header에 저장
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.AT_HEADER_STRING, JwtProperties.TOKEN_PREFIX + tokenList.get(0));

        ResponseCookie responseCookie = ResponseCookie.from(JwtProperties.RT_HEADER_STRING, tokenList.get(1))
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();

        res.addHeader("Set-Cookie", responseCookie.toString());

        //response body 설정
        UserCreateResponseDto userResponseDto = userService.getUserByToken(tokenList.get(0));
        SingleResponse<UserCreateResponseDto> response = responseService.getResponse(userResponseDto,
                                                                CodeStatus.SUCCESS_SOCIAL_LOGIN);

        return ResponseEntity.ok().headers(headers).body(response);
    }

    /**
     * refresh token으로 access token 재발급
     * @param request
     * @return
     */
    @GetMapping("/token/refresh")
    public ResponseEntity refresh(HttpServletRequest request){

        List<String> tokenList = tokenService.reissueRefreshToken(request.getHeader(JwtProperties.RT_HEADER_STRING)
                                                                    .replace(JwtProperties.TOKEN_PREFIX,""));

        //발급 받은 jwtToken, refreshToken header에 저장
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.AT_HEADER_STRING, JwtProperties.TOKEN_PREFIX + tokenList.get(0));
        headers.add(JwtProperties.RT_HEADER_STRING, JwtProperties.TOKEN_PREFIX + tokenList.get(1));

        //response body 설정
        UserCreateResponseDto userResponseDto = userService.getUserByToken(tokenList.get(0));
        SingleResponse<UserCreateResponseDto> response = responseService.getResponse(userResponseDto,
                                                                CodeStatus.SUCCESS_TOKEN_REISSUED);

        return ResponseEntity.ok().headers(headers).body(response);
    }

}
