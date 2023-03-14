package dnd.dnd10_backend.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.oauth.domain.KakaoProfile;
import dnd.dnd10_backend.user.oauth.domain.OauthToken;
import dnd.dnd10_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.JWT.require;

/**
 * 패키지명 dnd.dnd10_backend.user.service
 * 클래스명 TokenService
 * 클래스설명 토큰 관련 서비스
 * 작성일 2023-02-02
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-07] 프론트 local 판단해서 redirect_uri 변경 - 원지윤
 * [2023-02-08] 카카오 프로필 삭제 - 원지윤
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;

    //환경 변수 가져오기
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String client_id;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    String client_secret;

    /**
     * 인가 코드를 통해 access_token 발급
     * @param code 인가 코드
     * @return access_token
     */
    public OauthToken getAccessToken(String code, boolean isLocal) {
        String redirect_uri = isLocal ? "http://localhost:3000/login/oauth2/callback"
                : "https://fabulous-pony-8bfa39.netlify.app/login/oauth2/callback";
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", code);
        params.add("client_secret", client_secret);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oauthToken;
    }

    /**
     * 신규 사용자 저장 및 JWT토큰 생성하는 함수
     * @param token access_token
     * @return access token과 refresh token List
     */
    public List<String> saveUserAndGetToken(String token) {
        KakaoProfile profile = findProfile(token);
        List<String> tokenList = new ArrayList<>();

        if(profile.getKakao_account().getEmail().equals(null))
            throw new CustomerNotFoundException("");

        User user = userRepository.findByKakaoEmail(profile.getKakao_account().getEmail());

        if(user == null) {
            user = User.builder()
                    .kakaoId(profile.getId())
                    .kakaoNickname(profile.getKakao_account().getProfile().getNickname())
                    .kakaoEmail(profile.getKakao_account().getEmail())
                    .wage(0)
                    .userRole("ROLE_USER").build();

            userRepository.save(user);
        }

        //List에 각각 access token과 refresh token 차례로 넣어줌
        tokenList.add(createToken(user));
        tokenList.add(createRefreshToken(user));

        return tokenList;
    }

    /**
     * JWT토큰 생성하는 함수
     * @param user 사용자
     * @return 발급한 JWT토큰
     */
    public String createToken(User user) {

        String jwtToken = JWT.create()

                .withSubject(user.getKakaoEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.AT_EXP_TIME))

                .withClaim("id", user.getUserCode())
                .withClaim("email", user.getKakaoEmail())
                .withClaim("nickname", user.getKakaoNickname())

                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        return jwtToken;
    }

    /**
     * refresh토큰 생성하는 함수
     * @param user 사용자
     * @return 발급한 refresh token
     */
    public String createRefreshToken(User user) {

        String refreshToken = JWT.create()

                .withSubject(user.getKakaoEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.RT_EXP_TIME))

                .withClaim("id", user.getUserCode())

                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return refreshToken;
    }

    /**
     * refresh token을 받아 access token과 refresh toekn 재발급
     * @param token refresh token
     * @return access token과 refresh token List
     */
    public List<String> reissueRefreshToken(String token){
        List<String> tokenList = new ArrayList<>();
        //refreshToken 만료 확인
        if(!validateToken(token)){ //refresh token까지 만료 되었을 때
            throw new CustomerNotFoundException(CodeStatus.REFRESH_TOKEN_EXPIRED);
        }

        Long userCode = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("id").asLong();

        User user = userRepository.findByUserCode(userCode);

        tokenList.add(createToken(user));
        tokenList.add(createRefreshToken(user));

        return tokenList;
    }

    /**
     * 토큰 정보를 검증하는 메서드
     * @param token 토큰
     * @return 토큰 검증 여부
     */
    public boolean validateToken(String token) {
        Long userCode = null;
        try {
            userCode = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("id").asLong();
            return true;
        }catch (TokenExpiredException e) {
            //토큰이 만료되었습니다.
            e.printStackTrace();
        } catch (JWTVerificationException e) {
            //유효하지 않은 토큰입니다.
            e.printStackTrace();
        }
        return false;
    }

    /**
     * userCode로 권한 찾는 메소드
     * @param userCode
     * @return
     */
    public Authentication getAuthentication(Long userCode) {
        User user = userRepository.findByUserCode(userCode);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getKakaoEmail());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 카카오 서버에 접근해서 사용자의 정보를 받아오는 함수
     * @param token access token
     * @return 사용자의 정보
     */
    public KakaoProfile findProfile(String token) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }
}
