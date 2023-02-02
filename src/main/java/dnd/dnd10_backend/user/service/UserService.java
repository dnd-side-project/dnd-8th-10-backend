package dnd.dnd10_backend.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.dnd10_backend.auth.domain.Token;
import dnd.dnd10_backend.auth.repository.TokenRepository;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.dto.request.UserRequestDto;
import dnd.dnd10_backend.user.dto.request.UserSaveRequestDto;
import dnd.dnd10_backend.user.dto.response.UserResponseDto;
import dnd.dnd10_backend.user.oauth.domain.KakaoProfile;
import dnd.dnd10_backend.user.oauth.domain.OauthToken;
import dnd.dnd10_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.JWT.create;
import static com.auth0.jwt.JWT.require;

/**
 * 패키지명 dnd.dnd10_backend.user.service
 * 클래스명 UserService
 * 클래스설명
 * 작성일 2023-01-18
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-01-21] refresh token에 대한 내용 수정 - 원지윤
 * [2023-02-02] token관련 service 분리 - 원지윤
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    /**
     * 토큰 정보로 사용자를 조회하는 메소드
     * @param token access token
     * @return
     */
    public UserResponseDto getUserByToekn(String token){
        Long userCode = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("id").asLong();
        User user = userRepository.findByUserCode(userCode);
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);
        return UserResponseDto.of(user);
    }

    /**
     * 이메일로 사용자를 조회하는 메소드
     * @param requestDto
     * @return
     */
    public UserResponseDto getUserByEmail(UserRequestDto requestDto){
        //user 찾기
        User user = userRepository.findByKakaoEmail(requestDto.getKakaoEmail());
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        return UserResponseDto.of(user);
    }

    /**
     * 사용자 등록하는 메소드
     * @param requestDto client한테서 받아온 사용자 정보
     * @return UserResponseDto
     */
    public UserResponseDto saveUser(UserSaveRequestDto requestDto){
        //user 찾기
        User user = userRepository.findByKakaoEmail(requestDto.getKakaoEmail());
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        //requestDto로 user 정보 update
        user.updateUser(requestDto);

        //user 정보 저장
        userRepository.save(user);

        return UserResponseDto.of(user);
    }
}