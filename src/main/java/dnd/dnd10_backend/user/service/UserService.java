package dnd.dnd10_backend.user.service;

import com.auth0.jwt.algorithms.Algorithm;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.dto.request.UserSaveRequestDto;
import dnd.dnd10_backend.user.dto.response.UserResponseDto;
import dnd.dnd10_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public UserResponseDto getUserByToekn(final String token){
        Long userCode = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("id").asLong();
        User user = userRepository.findByUserCode(userCode);
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);
        return UserResponseDto.of(user);
    }

    /**
     * 이메일로 사용자를 조회하는 메소드
     * @param token access token
     * @return
     */
    public UserResponseDto getUserByEmail(final String token){
        String email = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("email").asString();
        //user 찾기
        User user = userRepository.findByKakaoEmail(email);
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        return UserResponseDto.of(user);
    }

    /**
     * 사용자 등록하는 메소드
     * @param requestDto client한테서 받아온 사용자 정보
     * @return UserResponseDto
     */
    public UserResponseDto saveUser(UserSaveRequestDto requestDto, final String token){
        String email = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("email").asString();
        //user 찾기
        User user = userRepository.findByKakaoEmail(email);
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        //requestDto로 user 정보 update
        user.updateUser(requestDto);

        //user 정보 저장
        userRepository.save(user);

        return UserResponseDto.of(user);
    }
}